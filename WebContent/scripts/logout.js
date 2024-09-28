export function handleLogoutResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    if (resultDataJson["status"] === "success") {
        Toastify({
            text: "Logged Out",
            gravity: "top",
            position: "center",
            duration: 1500,
            close: true,
            backgroundColor: "#2ea89f"
        }).showToast();

        setTimeout(() => {
            window.location.replace("login.jsp");
        }, 1500);
    } else {
        Toastify({
            text: "Error!",
            gravity: "top",
            position: "center",
            duration: 1500,
            close: true,
            backgroundColor: "#ca3f49",
        }).showToast();
    }
}

function logout() {
    $.ajax(
        "api/logout", {
            method: "POST",
            success: handleLogoutResult
        }
    );
}
$("#logout-btn").on("click",logout)