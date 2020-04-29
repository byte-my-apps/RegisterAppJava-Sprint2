package edu.uark.registerapp.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestBody(required = false) String transactionId,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
        Transaction transaction = new Transaction();
        if (transactionId != null) {
          transaction = new Transaction(transactionRepository.findById(UUID.fromString(transactionId)).get());
        }
        
        System.out.println(transactionId);

        System.out.println(partialLookupCode);    
        Product[] productArray = this.productByPartialLookupCodeQuery.setLookupCode(partialLookupCode).execute();
        for(Product product : productArray) {
          System.out.println(product.getId());
        }
        UUID employeeId = activeUserRepository.findBySessionKey(request.getSession().getId()).get().getEmployeeId();
        transactionCreateCommand.setApiTransaction(transaction).setEmployeeId(employeeId).execute();
        List<TransactionEntry> transactionEntries = new LinkedList<TransactionEntry>();
        Product productToAdd = productArray[0];
        System.out.println(productToAdd.getId());
        
        transactionEntries.add(
          new TransactionEntry( 
          ).setProductId(
            productToAdd.getId()));
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

    transaction = this.transactionCreateCommand
    .execute();

			transaction
				.setRedirectUrl(
					ViewNames.TRANSACTION.getRoute().concat(
						this.buildInitialQueryParameter(
							"transactionId",
              transaction.getId().toString())));		
              
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
}
