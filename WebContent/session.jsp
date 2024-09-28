<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fabflix</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/single-star.css">
    <link rel="stylesheet" type="text/css" href="styles/header.css">
</head>
<body>
<%@ include file="./components/header/header.jsp" %>
<main>
    <div class="container">
        <h1>Session</h1>
        <div class="session-wrapper" id="session-data"></div>
    </div>
</main>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script type="module" src="./scripts/session.js"></script>
</body>
</html>