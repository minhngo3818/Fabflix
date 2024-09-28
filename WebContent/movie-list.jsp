<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fabflix</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/movie-list.css">
    <link rel="stylesheet" type="text/css" href="styles/header.css">
</head>
<body>
    <%@ include file="./components/header/header.jsp" %>
    <main>
        <div class="container">
            <h1>Movie List</h1>
            <table class="table-list" id="movie-list-table">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th class="th-sortable" aria-sort="ascending">
                            <button>
                                Title
                                <span aria-hidden="true"></span>
                            </button>
                        </th>
                        <th>Year</th>
                        <th>Director</th>
                        <th class="th-sortable" aria-sort="descending">
                            <button>
                                Rating
                                <span aria-hidden="true"></span>
                            </button>
                        </th>
                        <th>Genres</th>
                        <th>Stars</th>
                        <th>Price (USD)</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody id="movie-list-tbody"></tbody>
            </table>
            <div id="pagination">
                <div class="pagination-links" id="page-links-section">
                    <button id="prev-page" class="page-btn">Prev</button>
                    <button id="next-page" class="page-btn">Next</button>
                </div>
                <div class="pagination-info">
                    <span>
                        <label for="page-count-selection">Page size: </label>
                        <select id="page-count-selection">
                            <option value=10>10</option>
                            <option value=25>25</option>
                            <option value=50>50</option>
                            <option value=100>100</option>
                        </select>
                    </span>
                    <div class="total-records-wrapper">Total records: <span id="total-records"></span></div>
                </div>
            </div>
        </div>
        
    </main>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <script type="module" src="scripts/pagination.js"></script>
    <script type="module" src="scripts/movie-list.js"></script>
</body>
</html>