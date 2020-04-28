document.addEventListener("DOMContentLoaded", function(event) {
	getStartTransactionActionElement().addEventListener(
		"click",
		() => { window.location.assign("/transaction"); });
		// button now goes to the Transaction page
	/*
	getEditTransactionActionElement().addEventListener(
		"click",
		() => { window.location.assign("/transactionListing"); });
		// button now goes to the Edit Transaction page
	*/
	getViewProductsActionElement().addEventListener(
		"click",
		() => { window.location.assign("/productListing"); });

	getCreateEmployeeActionElement().addEventListener(
		"click",
		() => { window.location.assign("/employeeDetail"); });

	getProductSalesReportActionElement().addEventListener(
		"click",
		() => { displayError("Functionality has not yet been implemented."); });

	getCashierSalesReportActionElement().addEventListener(
		"click",
		() => { displayError("Functionality has not yet been implemented."); });
});

// Getters and setters
function getViewProductsActionElement() {
	return document.getElementById("viewProductsButton");
}

function getCreateEmployeeActionElement() {
	return document.getElementById("createEmployeeButton");
}

function getStartTransactionActionElement() {
	return document.getElementById("startTransactionButton");
}
/*
function getEditTransactionActionElement() {
	return document.getElementById("editTransactionButton");
}
*/
function getProductSalesReportActionElement() {
	return document.getElementById("productSalesReportButton");
}

function getCashierSalesReportActionElement() {
	return document.getElementById("cashierSalesReportButton");
}
// End getters and setters
