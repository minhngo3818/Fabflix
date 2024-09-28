<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fabflix</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/sales.css">
    <link rel="stylesheet" type="text/css" href="styles/header.css">
</head>
<body>
<%@ include file="./components/header/header.jsp" %>
<main>
    <div class="container">
        <h2>Order Confirmation</h2>
        <table class="table-list">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Movie Title</th>
                    <th>Quantity</th>
                    <th>Price (USD)</th>
                    <th>Total (USD)</th>
                    <th>Sale Date</th>
                </tr>
            </thead>
            <tbody id="order-list-tbody"></tbody>
        </table>
    </div>
</main>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script type="module" src="scripts/order-confirmation.js"></script>
</body>
</html>