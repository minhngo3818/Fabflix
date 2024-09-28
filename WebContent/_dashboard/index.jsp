<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="styles/dash-index.css">
    <link rel="stylesheet" type="text/css" href="../styles/header.css">
    <title>Fabflix</title>
</head>
<body>
  <%@ include file="../components/header/dashboard-header.jsp" %>
  <main>
      <div class="container">
          <h1>Database Metadata</h1>
          <div class="metadb-container" id="metadb-list">
          </div>
      </div>
  </main>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
  <script type="module" src="scripts/dash-index.js"></script>
</body>
</html>