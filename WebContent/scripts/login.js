let login_form = $("#login-form");

/**
 * Handle the data returned by LoginServlet
 * @param resultJson jsonObject
 */
function handleLoginSuccess(resultJson) {
    // If login succeeds, it will show toast success message
    // and redirect the user to index.jsp
    Toastify({
        text: "Login Success!",
        gravity: "top",
        position: "center",
        duration: 1500,
        close: true,
        backgroundColor: "#2ea89f"
    }).showToast();

    setTimeout(() => {
        window.location.replace("index.jsp");
    }, 1500);
}

function handleLoginError(resultString) {
    let resultJson = JSON.parse(resultString)

    Toastify({
        text: resultJson["message"],
        gravity: "top",
        position: "center",
        duration: 2000,
        close: true,
        backgroundColor: "#ca3f49",
    }).showToast();

    setTimeout(() => {
        window.location.reload()
    }, 1000)
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    let url = new URL(window.location.href);
    let loginAPI = url.pathname.includes("_dashboard") ? "api/login-employee" : "api/login"

    // var response = grecaptcha.getResponse();
    // if (response.length === 0) {
    //     // reCAPTCHA not verified, prevent form submission
    //     Toastify({
    //         text: "reCAPTCHA not verified",
    //         gravity: "top",
    //         position: "center",
    //         duration: 1000,
    //         close: true,
    //         style: {
    //             background: "#ca3f49",
    //         }
    //     }).showToast();
    // } else {
    //     jQuery.ajax({
    //         dataType: "json",
    //         method: "POST",
    //         url: loginAPI,
    //         data: login_form.serialize(),
    //         success: (result) => handleLoginSuccess(result),
    //         error: (jqXHR) => handleLoginError(jqXHR.responseText)
    //     })
    // }

    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: loginAPI,
        data: login_form.serialize(),
        success: (result) => handleLoginSuccess(result),
        error: (jqXHR) => handleLoginError(jqXHR.responseText)
    })
}

// Bind the submit action of the form to a handler function
login_form.submit(submitLoginForm);