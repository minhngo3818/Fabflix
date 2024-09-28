<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no"
    name="viewport">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="../styles/login.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <title>Fabflix</title>
</head>
<body>
    <main>
        <div class="login-container">
            <form method="post" id="login-form" action="#">
                <h1 class="title">Employee Login</h1>
                <div class="input-box">
                    <input name="email" type="text" id="email" placeholder="Enter email" required/>
                    <i class='bx bxs-user'></i>
                </div>
                <div class="input-box">
                    <input name="password" type="password" id="password" placeholder="Enter password" required/>
                    <i class='bx bxs-lock-alt' ></i>
                </div>
<%--                <div class="g-recaptcha" data-sitekey="6LcR884pAAAAAIVWf_oFEJq3oayBvTaN27iXZrsV"></div>--%>
                <button class="submit-btn" type="submit">Login</button>
            </form>
        </div>
    </main>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script src='https://www.google.com/recaptcha/api.js' type="text/javascript"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <script src="../scripts/login.js"></script>
</body>
</html>