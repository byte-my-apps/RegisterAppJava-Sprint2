package edu.uark.registerapp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.transactions.TransactionCreateCommand;
import edu.uark.registerapp.commands.transactions.TransactionQuery;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.models.entities.TransactionEntryEntity;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;
import edu.uark.registerapp.commands.products.ProductByPartialLookupCodeQuery;
import edu.uark.registerapp.commands.transactions.*;
import edu.uark.registerapp.models.api.Product;
import edu.uark.registerapp.models.api.TransactionEntry;

@Controller
@RequestMapping(value = "/transaction")
public class TransactionRouteController extends BaseRouteController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTransaction(
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request
	) {
        final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        } 
        
        ModelAndView modelAndView =
			this.setErrorMessageFromQueryString(
				new ModelAndView(ViewNames.TRANSACTION.getViewName()),
                queryParameters);
        
        modelAndView.addObject(ViewModelNames.TRANSACTIONS.getValue(), this.transactionsQuery.execute());

        return modelAndView;
    }

    // Properties
	@Autowired
    private TransactionCreateCommand transactionCreateCommand;
    @Autowired
    private TransactionsQuery transactionsQuery;
}
