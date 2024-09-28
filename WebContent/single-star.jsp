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
        <div class="single-star-container">
            <h1>Single Star Page</h1>
            <div class="single-star-wrapper">
                <div id="star-info"></div>
                <div id="star-movies-info">
                    <h3>Movie List</h3>
                    <table class="table-list" id="table-list">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Year</th>
                                <th>Director</th>
                            </tr>
                        </thead>
                        <tbody id="star-movies-tbody"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
    <script type="module" src="./scripts/single-star.js"></script>
</body>
</html>