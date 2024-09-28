<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/styles.css">
    <link rel="stylesheet" type="text/css" href="../styles/header.css">
    <title>Fabflix</title>
</head>
<body>
    <%@ include file="../components/header/dashboard-header.jsp" %>
    <main>
        <div class="container">
            <h1>Add Movie</h1>
            <form class="vertical-form" method="post" id="add-movie-db-form" action="#">
                <div class="form-section-full">
                    <div class="form-field">
                        <label for="title-input">Title:</label>
                        <input class="primary-input"
                               name="title"
                               id="title-input"
                               type="text"
                               placeholder="Inception"
                               required />
                    </div>
                </div>
                <div class="form-section-full">
                    <div class="form-field">
                        <label for="director-input">Director:</label>
                        <input class="primary-input"
                               name="director"
                               id="director-input"
                               type="text"
                               placeholder="Christopher Nolan"/>
                    </div>
                </div>
                <div class="form-section-right-split">
                    <div class="form-field">
                        <label for="genre-input">Genre:</label>
                        <input class="primary-input"
                               name="genre"
                               id="genre-input"
                               type="text"
                               placeholder="Sci-fi"
                               required />
                    </div>
                    <div class="form-field">
                        <label for="year-input">Year:</label>
                        <input class="primary-input"
                               name="year"
                               id="year-input"
                               type="text"
                               inputmode="numeric"
                               placeholder="2010"/>
                    </div>
                </div>
                <hr class="divider" />
                <div class="form-section-right-split">
                    <div class="form-field">
                        <label for="star-name-input">Star Name:</label>
                        <input class="primary-input"
                               name="starName"
                               id="star-name-input"
                               type="text"
                               placeholder="Cillian Murphy"
                               required />
                    </div>
                    <div class="form-field">
                        <label for="birth-year-input">Birth Year:</label>
                        <input class="primary-input"
                               name="birthYear"
                               id="birth-year-input"
                               type="text"
                               inputmode="numeric"
                               placeholder="1976"/>
                    </div>
                </div>
                <button class="primary-btn spread" type="submit">Submit</button>
            </form>
            <div class="result-container" id="add-result">
                <h2>Result Log</h2>
            </div>
        </div>
    </main>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
<script type="module" src="scripts/dash-add-movie.js"></script>
</body>
</html>