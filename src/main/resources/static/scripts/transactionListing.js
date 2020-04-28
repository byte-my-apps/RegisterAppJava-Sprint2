document.addEventListener("DOMContentLoaded", () => {
	const transactionListElements = document.getElementById("transactionListing").children;

	for (let i = 0; i < transactionListElements.length; i++) {
		transactionListElements[i].addEventListener("click", transactionClick);
	}
});

function findClickedListItemElement(clickedTarget) {
	if (clickedTarget.tagName.toLowerCase() === "li") {
		return clickedTarget;
	} else {
		let ancestorIsListItem = false;
		let ancestorElement = clickedTarget.parentElement;

		while (!ancestorIsListItem && (ancestorElement != null)) {
			ancestorIsListItem = (ancestorElement.tagName.toLowerCase() === "li");

			if (!ancestorIsListItem) {
				ancestorElement = ancestorElement.parentElement;
			}
		}

		return (ancestorIsListItem ? ancestorElement : null);
	}
}

function transactionClick(event) {
	let listItem = findClickedListItemElement(event.target);
	/*
	// either do Transaction Detail or let it go to Transaction Summary??
	window.location.assign(
		"/transactionDetail/"
		+ listItem.querySelector("input[name='productId'][type='hidden']").value);
	*/
}
