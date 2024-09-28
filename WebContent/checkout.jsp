<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fabflix</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/checkout.css">
    <link rel="stylesheet" type="text/css" href="styles/header.css">
</head>
    <body>
        <%@ include file="./components/header/header.jsp" %>
        <main>
            <div class="container">
                <div class="container-md">
                    <div class="page-title-wrapper ">
                        <h2 class="page-title-1">Check out</h2>
                    </div>
                    <hr class="divider" />
                    <div class="cart">
                        <div class="items" id="cart-items">
                        </div>
                        <hr class="divider" />
                        <div class="cart-summary">
                            <div class="summary-section">
                                <span class="summary-label">Total Count: </span>
                                <span class="summary-value" id="total-count">--</span>
                            </div>
                            <div class="summary-section">
                                <span class="summary-label">Total Price: </span>
                                <span class="summary-value" id="total-price">---</span>
                            </div>
                        </div>
                        <div class="btn-wrapper">
                            <button class="primary-btn" onclick="window.location='payment.jsp'">Proceed to payment</button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
        <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
        <script type="module" src="scripts/checkout.js"></script>
    </body>
</html>