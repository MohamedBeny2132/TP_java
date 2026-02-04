<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce" %>
<%
    Annonce annonce = (Annonce) request.getAttribute("annonce");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier l'annonce</title>
</head>
<body>

<h1>Modifier l'annonce</h1>

<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>

<form method="post" action="${pageContext.request.contextPath}/annonce/update">
    <input type="hidden" name="id" value="<%= annonce.getId() %>">

    <label for="title">Titre :</label><br>
    <input type="text" id="title" name="title" value="<%= annonce.getTitle() %>" required><br><br>

    <label for="description">Description :</label><br>
    <textarea id="description" name="description" rows="5" cols="40" required><%= annonce.getDescription() %></textarea><br><br>

    <label for="adress">Adresse :</label><br>
    <input type="text" id="adress" name="adress" value="<%= annonce.getAdress() %>" required><br><br>

    <label for="mail">Email :</label><br>
    <input type="email" id="mail" name="mail" value="<%= annonce.getMail() %>" required><br><br>

    <input type="submit" value="Mettre à jour">
</form>

<a href="${pageContext.request.contextPath}/annonce/list">Retour à la liste</a>

</body>
</html>
