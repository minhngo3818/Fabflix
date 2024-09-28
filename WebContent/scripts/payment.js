let payment_form = $("#payment-form");

function handleSubmitPayment(formSubmitEvent) {
    formSubmitEvent.preventDefault();

    console.log(payment_form.serialize())

    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/payment",
        data: payment_form.serialize(),
        success: (resultData) => handleResponseStatus(resultData)
    })
}

payment_form.submit(handleSubmitPayment)

function handleResponseStatus(dataJsonObject) {
    if (dataJsonObject["status"] === "success") {
        Toastify({
            text: dataJsonObject["message"],
            gravity: "top",
            position: "center",
            duration: 2000,
            close: true,
            backgroundColor: "#2ea89f"
        }).showToast();

        setTimeout(() => {
            window.location.href="./order-confirmation.jsp";
        }, 2000);
    } else {
        // Show toast message if login failed
        Toastify({
            text: dataJsonObject["message"],
            gravity: "top",
            position: "center",
            duration: 2000,
            close: true,
            backgroundColor: "#ca3f49",
        }).showToast();
    }
}


function handleTotalPayment(dataJsonObject) {
    const items = dataJsonObject["data"];

    let total = 0;
    items.forEach(item => {
        total += item["unitPrice"] * item["quantity"];
    });

    document.getElementById("total-payment").innerText = `$${total.toFixed(2)}`;
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/cart",
    success: (resultData) => handleTotalPayment(resultData)
})