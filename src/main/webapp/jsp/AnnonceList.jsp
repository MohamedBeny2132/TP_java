<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce" %>
<%@ page import="java.util.List" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des annonces</title>
</head>
<body>

<h1>Liste des annonces</h1>

<table border="1">
    <tr>
        <th>Id</th>
        <th>Titre</th>
        <th>Description</th>
        <th>Adresse</th>
        <th>Email</th>
        <th>Date</th>
        <th>Actions</th>
    </tr>

    <%
        List<Annonce> annonces = (List<Annonce>) request.getAttribute("annonces");
        if (annonces != null) {
            for (Annonce a : annonces) {
    %>
    <tr>
        <td><%= a.getId() %></td>
        <td><%= a.getTitle() %></td>
        <td><%= a.getDescription() %></td>
        <td><%= a.getAdress() %></td>
        <td><%= a.getMail() %></td>
        <td><%= a.getDate() %></td>
        <td>
            <a href="${pageContext.request.contextPath}/annonce/update?id=<%= a.getId() %>">Modifier</a> |
            <a href="${pageContext.request.contextPath}/annonce/delete?id=<%= a.getId() %>"
               onclick="return confirm('Voulez-vous vraiment supprimer cette annonce ?');">Supprimer</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<br>
<a href="${pageContext.request.contextPath}/annonce/add">Ajouter une nouvelle annonce</a>

<!-- Pagination -->
<%
    int currentPage = (int) request.getAttribute("currentPage");
    int nbrPages = (int) request.getAttribute("nbrPages");
    AnnonceSearchParams params = (AnnonceSearchParams) request.getAttribute("params");

    String queryParams = "";
    if (params != null) {
        if (params.getKeyword() != null) queryParams += "&keyword=" + params.getKeyword();
        if (params.getStatus() != null) queryParams += "&status=" + params.getStatus();
        if (params.getCategoryId() != null) queryParams += "&categoryId=" + params.getCategoryId();
    }
%>

<div>
    <%
        if (currentPage > 1) {
    %>
    <a href="?page=<%= currentPage - 1 %>&size=<%= params.getSize() %><%= queryParams %>">← Précédent</a>
    <%
        }

        for (int i = 1; i <= nbrPages; i++) {
            if (i == currentPage) {
    %>
    <strong><%= i %></strong>
    <%
    } else {
    %>
    <a href="?page=<%= i %>&size=<%= params.getSize() %><%= queryParams %>"><%= i %></a>
    <%
            }
        }

        if (currentPage < nbrPages) {
    %>
    <a href="?page=<%= currentPage + 1 %>&size=<%= params.getSize() %><%= queryParams %>">Suivant →</a>
    <%
        }
    %>
</div>

</body>
</html>
