document.addEventListener("DOMContentLoaded", function(event) {
	getBackActionElement().addEventListener(
		"click",
		() => { window.location.assign("/transaction"); });

	getSubmitTransActionElement().addEventListener(
		"click",
		() => { window.location.assign("/mainMenu") });
		// might want to return back to the Main Menu page with a message saying that the transaction has been submitted
		// once clicked, the database is updated
});

function calculatePrice() {
	var table = document.getElementById("TransactionSummary");
	var totalPrice = 0;

	for(var i = 1; i < table.rows.length - 1; i++) {
		totalPrice += parseInt(table.rows[i].cells[2].innerHTML);
	}
	return totalPrice;
}

function calculateQuantity() {
	var table = document.getElementById("TransactionSummary");
	var totalQuantity = 0;

	for(var i = 1; i < table.rows.length - 1; i++) {
		totalQuantity += parseInt(table.rows[i].cells[1].innerHTML);
	}
	return totalQuantity;
}

// Getters and setters
function getBackActionElement() {
	return document.getElementById("backButton");
}

function getSubmitTransActionElement() {
    return document.getElementById("submitButton");
}
// End getters and setters