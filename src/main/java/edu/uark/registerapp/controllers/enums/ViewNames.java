package edu.uark.registerapp.controllers.enums;

public enum ViewNames {
	SIGN_IN("signIn", "/"),
	MAIN_MENU("mainMenu"),
	PRODUCT_DETAIL("productDetail"),
	EMPLOYEE_DETAIL("employeeDetail"),
	TRANSACTION("transaction"),
	TRANSACTION_SUM("transactionSummary"),
	PRODUCT_LISTING("productListing");
	
	public String getRoute() {
		return this.route;
	}
	public String getViewName() {
		return this.viewName;
	}

	private String route;
	private String viewName;

	private ViewNames(final String viewName) {
		this(viewName, "/".concat(viewName));
	}

	private ViewNames(final String viewName, final String route) {
		this.route = route;
		this.viewName = viewName;
	}
}
