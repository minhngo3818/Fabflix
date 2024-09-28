<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Fabflix</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/payment.css">
  <link rel="stylesheet" type="text/css" href="styles/header.css">
</head>
<body>
  <%@ include file="./components/header/header.jsp" %>
  <main>
    <div class="container">
      <div class="container-sd">
        <div class="page-title-wrapper ">
          <h2 class="page-title-1">Payment</h2>
        </div>
        <hr class="divider" />
        <form method="post" id="payment-form" action="#">
          <div class="form-section-name">
            <div class="form-field">
              <label for="firstname">First Name</label>
              <input class="primary-input" name="firstname" id="firstname" type="text" placeholder="John" required/>
            </div>
            <div class="form-field">
              <label for="lastname">Last Name</label>
              <input class="primary-input" name="lastname" id="lastname" type="text" placeholder="Doe" required/>
            </div>
          </div>
          <div class="form-section-card">
            <div class="form-field">
              <label for="credit-card">Credit Card</label>
              <input class="primary-input"
                     name="creditCardId"
                     id="credit-card"
                     type="text"
                     inputmode="numeric"
                     placeholder="1111222233334444"
                     required
              />
            </div>
            <div class="form-field">
              <label for="exp-date">Exp.</label>
              <input class="primary-input"
                     name="expDate"
                     id="exp-date"
                     type="text"
                     placeholder="yyyy/mm/dd"
                     required
              />
            </div>
          </div>
          <hr class="divider" />
          <div class="form-section">
            <span class="total-label">Total Payment: </span>
            <span class="total-value" id="total-payment">$50.00</span>
          </div>
          <div class="form-section">
            <button class="primary-btn" type="submit">Place Order</button>
          </div>
        </form>
      </div>
    </div>
  </main>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.devbridge-autocomplete/1.4.7/jquery.autocomplete.min.js"></script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
  <script type="module" src="scripts/payment.js"></script>
</body>
</html>