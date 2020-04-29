package edu.uark.registerapp.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import edu.uark.registerapp.commands.transactions.TransactionCreateCommand;
import edu.uark.registerapp.models.entities.ActiveUserEntity;
import edu.uark.registerapp.controllers.enums.ViewModelNames;
import edu.uark.registerapp.controllers.enums.ViewNames;

@Controller
@RequestMapping(value = "/transactionSummary")
public class TransactionSummaryRouteController extends BaseRouteController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showTransaction(
		@CookieValue(value = "transactionId", defaultValue = "") String transactionId,
		@RequestParam final Map<String, String> queryParameters,
		final HttpServletRequest request,
		final HttpServletResponse response
	) {

			Cookie cookie = new Cookie("transactionId", null);
			cookie.setMaxAge(0);
			cookie.setSecure(true);
			cookie.setHttpOnly(true);
			cookie.setPath("/");

			response.addCookie(cookie);

        final Optional<ActiveUserEntity> activeUserEntity =
			this.getCurrentUser(request);
		if (!activeUserEntity.isPresent()) {
			return this.buildInvalidSessionResponse();
        } 
        
        ModelAndView modelAndView =
			this.setErrorMessageFromQueryString(
				new ModelAndView(ViewNames.TRANSACTION_SUM.getViewName()),
                queryParameters);
        
        modelAndView.addObject(
			ViewModelNames.IS_ELEVATED_USER.getValue(),
            this.isElevatedUser(activeUserEntity.get()));
                
        return modelAndView;
    }

    // Properties
	@Autowired
	private TransactionCreateCommand transactionCreateCommand;
}
