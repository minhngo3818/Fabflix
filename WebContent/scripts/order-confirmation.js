function renderData(resultData) {
    let data = resultData["data"];
    let saleItemComponent = jQuery("#order-list-tbody");

    // Clear previous content
    saleItemComponent.empty();

    data.forEach(item => {
        let totalPrice = (item["unitPrice"] * item["quantity"]).toFixed(2)

        const salesHTML = `<tr data-index="${item["id"]}">
                <td>${item["id"]}</td>
                <td>${item["movieTitle"]}</td>
                <td>${item["quantity"]}</td>
                <td>$${item["unitPrice"]}</td>
                <td>${(item["unitPrice"] * item["quantity"]).toFixed(2)}</td>
                <td>${item["salesDate"]}</td>
            </tr>`;

        // Append the HTML markup to the saleItemComponent
        saleItemComponent.append(salesHTML);
    });
}


jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/sales",
    success: (resultData) => renderData(resultData)
})