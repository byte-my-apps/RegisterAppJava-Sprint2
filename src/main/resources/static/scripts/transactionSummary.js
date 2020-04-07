document.addEventListener("DOMContentLoaded", function(event) {
	getBackActionElement().addEventListener(
		"click",
		() => { window.location.assign("/mainMenu"); });

	getSubmitTransActionElement().addEventListener(
		"click",
		() => { });
});

// Getters and setters
function getBackActionElement() {
	return document.getElementById("backButton");
}

function getSubmitTransActionElement() {
    return document.getElementById("submitButton");
}
// End getters and setters