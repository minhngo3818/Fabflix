export function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


// Function to generate a random floating-point number between min and max (inclusive)
export function getRandomFloat(min, max) {
    return (Math.random() * (max - min) + min).toFixed(2);
}


export function handleResponseStatus(resultDataJson) {

    if (resultDataJson["status"] === "success") {
        Toastify({
            text: resultDataJson["message"],
            gravity: "top",
            position: "center",
            duration: 2000,
            close: true,
            backgroundColor: "#2ea89f"
        }).showToast();

    } else {
        // Show toast message if login failed
        Toastify({
            text: resultDataJson["message"],
            gravity: "top",
            position: "center",
            duration: 2000,
            close: true,
            backgroundColor: "#ca3f49",
        }).showToast();
    }
}