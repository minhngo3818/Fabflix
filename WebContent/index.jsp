<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no"
          name="viewport">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="styles/index.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link rel="stylesheet" type="text/css" href="styles/header.css">
    <title>Fabflix</title>
</head>

<body>
    <%@ include file="./components/header/header.jsp" %>
    <main>
        <div class="container">
            <h1 class="greetings">Welcome to Fabflix</h1>
            <h3 class="caption">Your ultimate destination for searching and purchasing movies!</h3>
            <div class="sub-section">
                <h3>Search your movie</h3>
                <form class="se-form" id="search-form">
                    <input class="se-input" id="se-title" type="text" placeholder="Title" />
                    <input class="se-input" id="se-year" type="text" inputmode="numberic" placeholder="Year" />
                    <input class="se-input" id="se-director" type="text" placeholder="Director" />
                    <input class="se-input" id="se-star-name" type="text" placeholder="Star's name" />
                    <button class="se-btn" type="submit">
                        <i class='bx bx-search'></i>
                    </button>
                </form>
            </div>
            <div class="sub-section">
                <h3>Browse your movie</h3>
                <div class="button-list">
                    <button class="primary-btn" type="button" id="genres-popup-btn">Genres<i class='bx bx-window-alt'></i></button>
                    <button class="primary-btn" type="button" id="title-popup-btn">Title<i class='bx bx-window-alt'></i></button>
                </div>
                <div class="popup-container" id="genres-popup-container">
                    <div class="popup" id="genres-popup">
                        <div class="popup-header">
                            <h2>Browse Genres</h2>
                        </div>
                        <div class="popup-body">
                            <div class="browse-genres" id="browse-genres">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="popup-container" id="title-popup-container">
                    <div class="popup" id="title-popup">
                        <div class="popup-header">
                            <h2>Browse Title</h2>
                        </div>
                        <div class="popup-body">
                            <div class="browse-title" id="browse-title">
                                <span id="alphabet-span">
                                    <p>Start with letter: </p>
                                </span>
                                <span id="nums-span">
                                    <p>Start with number :</p>
                                </span>
                                <span id="others-span">
                                    <p>Others: </p>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <script src="scripts/index.js"></script>
</body>
</html>

