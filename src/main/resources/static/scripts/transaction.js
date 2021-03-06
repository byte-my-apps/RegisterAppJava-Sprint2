document.addEventListener("DOMContentLoaded", function(event) {
	getSearchActionElement().addEventListener(
		"click",
		() => {
            const lookupCode = getSearch();
        });

	getMainMenuActionElement().addEventListener(
		"click",
		() => { window.location.assign("/mainMenu"); });

    // will need to add the listItem.querySelector() so transactionSummary will include the items in the Transaction
	getTransactionSummActionElement().addEventListener(
		"click",
		() => { window.location.assign("/transactionSummary"); });

	getItemElement1().addEventListener(
                "click",
                () => {removeItem1();});

        getItemElement2().addEventListener(
                "click",
                () => {removeItem2();});

        getItemElement3().addEventListener(
                "click",
                () => {removeItem3();});

        getItemElement4().addEventListener(
                "click",
                () => {removeItem4();});

        getItemElement5().addEventListener(
                "click",
                () => {removeItem5();});

        getItemElement6().addEventListener(
                "click",
                () => {removeItem6();});

        getItemElement7().addEventListener(
                "click",
                () => {removeItem7();});

        getItemElement8().addEventListener(
                "click",
                () => {removeItem8();});

        getItemElement9().addEventListener(
                "click",
                () => {removeItem9();});

});

function searchActionClick() {
    const partialLookupCode = document.getElementById("lookupCode").value;
    const partialLookupCodeIsDefined = (partialLookupCode.trim() !== "");

    const searchActionUrl = ("/api/transaction/"
        + (partialLookupCodeIsDefined ? partialLookupCode : ""));
    const searchRequest = {
    lookupCode: partialLookupCode
    };

    if (partialLookupCodeIsDefined) {
		ajaxPatch(searchActionUrl, searchRequest, (callbackResponse) => {
			if (isSuccessResponse(callbackResponse)) {
				completeSearchAction(callbackResponse);
			}
		});
	} else {
		ajaxPost(searchActionUrl, searchRequest, (callbackResponse) => {
			if (isSuccessResponse(callbackResponse)) {
				completeSearchAction(callbackResponse);
			}
		});
	}
}

function completeSearchAction(callbackResponse) {
	if (callbackResponse.data == null) {
		return;
	}

	if ((callbackResponse.data.redirectUrl != null)
		&& (callbackResponse.data.redirectUrl !== "")) {

		window.location.replace(callbackResponse.data.redirectUrl);
		return;
	}
}

// Getters and setters
function getSearch() {
    return document.getElementById("lookupCode");
}

function getSearchActionElement() {
    return document.getElementById("searchButton");
}

function getMainMenuActionElement() {
	return document.getElementById("mainMenuButton");
}

function getTransactionSummActionElement() {
    return document.getElementById("transactionSumButton");
}

function getItemElement1()
{
    return document.getElementById("item1");
}

function getItemElement2()
{
    return document.getElementById("item2");
}

function getItemElement3()
{
    return document.getElementById("item3");
}

function getItemElement4()
{
    return document.getElementById("item4");
}

function getItemElement5()
{
    return document.getElementById("item5");
}

function getItemElement6()
{
    return document.getElementById("item6");
}

function getItemElement7()
{
    return document.getElementById("item7");
}

function getItemElement8()
{
    return document.getElementById("item8");
}

function getItemElement9()
{
    return document.getElementById("item9");
}

function removeItem1() {
    document.getElementById("item1").innerHTML = "";
}
function removeItem2() {
    document.getElementById("item2").innerHTML = "";
}
function removeItem3() {
    document.getElementById("item3").innerHTML = "";
}
function removeItem4() {
    document.getElementById("item4").innerHTML = "";
}
function removeItem5() {
    document.getElementById("item5").innerHTML = "";
}
function removeItem6() {
    document.getElementById("item6").innerHTML = "";
}
function removeItem7() {
    document.getElementById("item7").innerHTML = "";
}
function removeItem8() {
    document.getElementById("item8").innerHTML = "";
}
function removeItem9() {
    document.getElementById("item9").innerHTML = "";
}
// End getters and setters











