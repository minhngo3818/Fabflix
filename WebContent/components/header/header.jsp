<header id="header-bar">
    <nav class="navbar">
        <a class="nav-url" href="./index.jsp"><h1>Fabflix</h1></a>
        <div class="search-fulltext-box" id="search-fulltext-box">
            <div class="search-dropdown active" >
                <form class="search-box" id="search-autocomplete-form">
                    <input name="keywords" id="autocomplete" type="text" placeholder="Enter keywords" autocomplete="off"/>
                    <button type="submit"><i class='bx bx-search'></i></button>
                </form>
                <div class="search-dropdown-menu" id="suggestion-results">
                </div>
            </div>
        </div>
        <ul class="nav-links">
            <li>
                <button class="nav-icon-btn"
                        onclick="window.location='./checkout.jsp'"
                        title="Checkout">
                    <i class='bx bx-cart' ></i>
                </button>
            </li>
            <li>
                <button class="nav-icon-btn"
                        onclick="window.location='./order-confirmation.jsp'"
                        title="Orders">
                    <i class='bx bx-list-check'></i>
                </button>
            </li>
            <li><button class="nav-btn" type="button" id="logout-btn">Logout</button></li>
        </ul>
    </nav>
</header>
<script type="module" src="${pageContext.request.contextPath}/scripts/logout.js"></script>
<script type="module" src="${pageContext.request.contextPath}/scripts/search.js"></script>