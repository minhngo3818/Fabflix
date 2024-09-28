import { renderCartItems, removeCartItem, updateCartItem } from "./cart-crud.js";

function handleData(dataJson) {
    renderCartItems(dataJson)
    loadListeners()
}

function loadListeners() {
    document.querySelectorAll('.item').forEach(item => {
        const addButton = item.querySelector('button[name="add-count-item-btn"]');
        const subtractButton = item.querySelector('button[name="subtract-count-item-btn"]');
        const removeButton = item.querySelector('button[name="remove-item-btn"]')
        addButton.addEventListener('click', changeItemCountHandler);
        subtractButton.addEventListener('click', changeItemCountHandler);
        removeButton.addEventListener("click", removeItemHandler)
    });
    updateTotals()
}


function changeItemCountHandler() {
    const item = this.closest('.item');
    const input = item.querySelector('input[name="counter-value"]');
    let value = input.value === null ? 1: parseInt(input.value)

    // Determine if the button is for increase (+) or decrease (-)
    const isIncrease = this.textContent === '+';
    if (isIncrease) {
        value++;
    } else {
        // Ensure the value doesn't go below 1
        value = Math.max(1, value - 1);
    }

    input.value = value;

    const itemData = {
        itemId: item.getAttribute("data-index"),
        quantity: value,
    }

    console.log("data: ", itemData)

    updateTotals()
    updateCartItem(itemData)
}

function removeItemHandler() {
    const item = this.closest('.item');
    const id = item.getAttribute("data-index")
    item.remove();
    updateTotals()

    removeCartItem(id)
}

function updateTotals() {
    let totalQuantity = 0;
    let totalPrice = 0;

    document.querySelectorAll('.item').forEach(item => {
        const quantity = parseInt(item.querySelector('input[name="counter-value"]').value);
        const price = parseFloat(item.querySelector('.price').textContent.replace('$', ''));
        totalQuantity += quantity;
        totalPrice += quantity * price;
    });

    // Update total quantity and total price display
    document.getElementById('total-count').textContent = totalQuantity;
    document.getElementById('total-price').textContent = '$' + totalPrice.toFixed(2);
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/cart",
    success: (resultData) => handleData(resultData)
})