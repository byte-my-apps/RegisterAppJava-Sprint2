package edu.uark.registerapp.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.models.repositories.TransactionEntryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.uark.registerapp.commands.products.ProductByPartialLookupCodeQuery;
import edu.uark.registerapp.commands.transactions.TransactionCreateCommand;
import edu.uark.registerapp.commands.transactions.TransactionDeleteCommand;
import edu.uark.registerapp.controllers.enums.QueryParameterNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.ApiResponse;
import edu.uark.registerapp.models.api.Employee;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.api.Transaction;
import edu.uark.registerapp.models.api.TransactionEntry;
import edu.uark.registerapp.models.repositories.ActiveUserRepository;
import edu.uark.registerapp.models.repositories.EmployeeRepository;
import edu.uark.registerapp.models.repositories.TransactionRepository;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionRestController extends BaseRestController {
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public @ResponseBody
  ApiResponse createTransaction(
      @RequestBody final Transaction transaction,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {

    final ApiResponse elevatedUserResponse =
        this.redirectUserNotElevated(
            request,
            response,
            ViewNames.TRANSACTION.getRoute());

    if (!elevatedUserResponse.getRedirectUrl().equals(StringUtils.EMPTY)) {
      return elevatedUserResponse;
    }

    List<TransactionEntry> transactionEntries = transactionCreateCommand.setApiTransaction(transaction).getTransactionEntries();
    
    return this.transactionCreateCommand
        .setTransactionEntries(transactionEntries)
        .execute();
  }    
    
  @RequestMapping(value = "/{partialLookupCode}", method = RequestMethod.PATCH)
  public @ResponseBody 
  ApiResponse createTransactionEntry(
    @PathVariable final String partialLookupCode,
    @CookieValue(value = "transactionId", defaultValue = "") String transactionId,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
        Transaction transaction = new Transaction();
        boolean newTransaction = true;

        if (transactionId != null && !transactionId.equals("")) {
          transaction = new Transaction(transactionRepository.findById(UUID.fromString(transactionId)).get());
          newTransaction = false;
        }
        Product[] productArray = this.productByPartialLookupCodeQuery.setLookupCode(partialLookupCode).execute();
        for(Product product : productArray) {
          System.out.println(product.getId());
        }
        UUID employeeId = activeUserRepository.findBySessionKey(request.getSession().getId()).get().getEmployeeId();

        List<TransactionEntry> transactionEntries = new LinkedList<TransactionEntry>();
        Product productToAdd = productArray[0];
        System.out.println(productToAdd.getId());
        
        transactionEntries.add(
          new TransactionEntry( 
          ).setProductId(
            productToAdd.getId()).setPrice(productToAdd.getPrice()).setQuantity(1));
        for(TransactionEntry entry : transactionEntries){
            System.out.println(entry.getId());
        }
    
        transactionCreateCommand.setTransactionEntries(transactionEntries);
        final ApiResponse elevatedUserResponse =
        this.redirectUserNotElevated(
            request,
            response,
            ViewNames.TRANSACTION.getRoute());

    if (!elevatedUserResponse.getRedirectUrl().equals(StringUtils.EMPTY)) {
      return elevatedUserResponse;
    }
    if (newTransaction) {
      transaction = transactionCreateCommand.setApiTransaction(transaction)
          .setEmployeeId(employeeId)
          .setTransactionEntries(transactionEntries)
          .execute();
    } else {
      for (TransactionEntry transactionEntry : transactionEntries) {
        TransactionEntryEntity transactionEntryEntity = new TransactionEntryEntity()
            .setPrice(transactionEntry.getPrice())
            .setProductId(transactionEntry.getProductId())
            .setQuantity(transactionEntry.getQuantity());
        transactionEntryEntity.setTransactionId(UUID.fromString(transactionId));

        this.transactionEntryRepository.save(transactionEntryEntity);
      }
    }

			transaction
				.setRedirectUrl(
					ViewNames.TRANSACTION.getRoute().concat(
						this.buildInitialQueryParameter(
							"transactionId",
              transaction.getId().toString())));

    request.setAttribute("transactionId", transaction.getId().toString());

    Cookie cookie = new Cookie("transactionId", transaction.getId().toString());
    response.addCookie(cookie);

    return transaction;
   
  }

  @RequestMapping(value = "/{transactionId}", method = RequestMethod.DELETE)
	public @ResponseBody ApiResponse deleteTransaction(
		@PathVariable final UUID transactionId, 
		final HttpServletRequest request,
		final HttpServletResponse response
	) {

		final ApiResponse elevatedUserResponse =
			this.redirectUserNotElevated(
				request,
				response,
				ViewNames.TRANSACTION.getRoute());

		if (!elevatedUserResponse.getRedirectUrl().equals(StringUtils.EMPTY)) {
			return elevatedUserResponse;
		}

		this.transactionDeleteCommand
			.setTransactionEntryId(transactionId)
			.execute();

		return new ApiResponse();
	}

  // Properties
  @Autowired
  private TransactionCreateCommand transactionCreateCommand;

  @Autowired
  private TransactionDeleteCommand transactionDeleteCommand;
  
  @Autowired
  private ProductByPartialLookupCodeQuery productByPartialLookupCodeQuery;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ActiveUserRepository activeUserRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionEntryRepository transactionEntryRepository;

}
