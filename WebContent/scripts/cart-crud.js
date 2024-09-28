function generateCartItemHTML(item) {
    return `
        <div class="item" data-index="${item.id}">
            <div class="item-section">
                <p class="item-title">${item.movieTitle}</p>
            </div>
            <div class="item-section">
                <button name="add-count-item-btn" type="button" class="btn-sm">-</button>
                <input name="counter-value" type="text" value="${item.quantity}" class="counter-value" readonly/>
                <button name="subtract-count-item-btn" type="button" class="btn-sm">+</button>
            </div>
            <div class="item-section">
                <button name="remove-item-btn" type="button" class="btn-sm delete-btn" value="${item.movieId}">
                    <i class='bx bx-trash'></i>
                </button>
            </div>
            <div class="price-section">
                <span class="price-label">Unit Price</span>
                <span class="price">$${item.unitPrice}</span>
            </div>
        </div>
    `;
}

// Function to render cart items
export function renderCartItems(dataJson) {
    const cartList = jQuery("#cart-items");
    const items = dataJson["data"];

    // Clear existing content
    cartList.empty();

    // Generate HTML for each cart item and append to table body
    items.forEach(item => {
        const cartItemHTML = generateCartItemHTML(item);
        cartList.append(cartItemHTML);
    });
}

export function addCartItem(data) {
    console.log(data)
    // Now you have rowData object containing all the information of the clicked row
    jQuery.ajax({
        dataType: "json",
        method: "POST",
        url: "api/cart",
        data: data,
        success: (result) => handleResponseStatus(result)
    })
}

export function updateCartItem(data) {
    jQuery.ajax({
        dataType: "json",
        method: "PUT",
        url: `api/cart?itemId=${data.itemId}&quantity=${data.quantity}`,
        success: (resultData) => handleResponseStatus(resultData)
    })
}

export function removeCartItem(id) {
    jQuery.ajax({
        dataType: "json",
        method: "DELETE",
        url: `api/cart?id=${id}`,
        success: (resultData) => handleResponseStatus(resultData)
    })
}

function handleResponseStatus(resultDataJson) {

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
