package edu.uark.registerapp.controllers;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uark.registerapp.commands.transactions.TransactionCreateCommand;
import edu.uark.registerapp.models.api.TransactionEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.uark.registerapp.commands.products.ProductCreateCommand;
import edu.uark.registerapp.commands.products.ProductDeleteCommand;
import edu.uark.registerapp.commands.products.ProductUpdateCommand;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.models.api.ApiResponse;
import edu.uark.registerapp.models.api.Product;

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

  // Properties
  @Autowired
  private TransactionCreateCommand transactionCreateCommand;
}
