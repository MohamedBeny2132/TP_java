<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Ajouter une annonce</title>
</head>
<body>

<h1>Ajouter une annonce</h1>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="post" action="${pageContext.request.contextPath}/annonce/add">
  <label for="title">Titre :</label><br>
  <input type="text" id="title" name="title" required><br><br>

  <label for="description">Description :</label><br>
  <textarea id="description" name="description" rows="5" cols="40" required></textarea><br><br>

  <label for="adress">Adresse :</label><br>
  <input type="text" id="adress" name="adress" required><br><br>

  <label for="mail">Email :</label><br>
  <input type="email" id="mail" name="mail" required><br><br>

  <input type="submit" value="Ajouter">
</form>

</body>
</html>
