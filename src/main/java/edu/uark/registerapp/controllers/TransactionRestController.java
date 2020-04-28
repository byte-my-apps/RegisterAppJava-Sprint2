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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.uark.registerapp.commands.products.ProductByPartialLookupCodeQuery;
import edu.uark.registerapp.commands.transactions.TransactionCreateCommand;
import edu.uark.registerapp.commands.transactions.TransactionDeleteCommand;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.ApiResponse;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.api.TransactionEntry;

@RestController
@RequestMapping(value = "/api/transaction")
public class TransactionRestController extends BaseRestController {
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public @ResponseBody
  ApiResponse createTransaction(
      @RequestBody final List<TransactionEntry> transactionEntries,
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

    return this.transactionCreateCommand
        .setTransactionEntries(transactionEntries)
        .execute();
  }    
    
  @RequestMapping(value = "/{partialLookupCode}", method = RequestMethod.PATCH)
  public @ResponseBody
  ApiResponse createTransactionEntry(
    @PathVariable final String partialLookupCode, 
    @RequestBody final LinkedList<TransactionEntry> transactionEntries,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
        Product productToAdd = productByPartialLookupCodeQuery.setLookupCode(partialLookupCode).execute()[0];
        transactionEntries.add(new TransactionEntry().setProductId(productToAdd.getId()));
        for(TransactionEntry entry : transactionEntries){
            System.out.println(entry.getId());
        }

    final ApiResponse elevatedUserResponse =
        this.redirectUserNotElevated(
            request,
            response,
            ViewNames.TRANSACTION.getRoute());

    if (!elevatedUserResponse.getRedirectUrl().equals(StringUtils.EMPTY)) {
      return elevatedUserResponse;
    }

    return transactionEntries.get(0);
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
}
