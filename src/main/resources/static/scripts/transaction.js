document.addEventListener("DOMContentLoaded", function(event) {
	getSearchActionElement().addEventListener(
		"click",
		() => {});

	getMainMenuActionElement().addEventListener(
		"click",
		() => { window.location.assign("/mainMenu"); });

	getTransactionSummActionElement().addEventListener(
		"click",
		() => { window.location.assign("/transactionSummary"); });
});

// Getters and setters
function getSearchActionElement() {
    return document.getElementById("searchButton");
}

function getMainMenuActionElement() {
	return document.getElementById("mainMenuButton");
}

function getTransactionSummActionElement() {
    return document.getElementById("transactionSumButton");
}
// End getters and setters