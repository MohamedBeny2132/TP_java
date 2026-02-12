<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des annonces</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 40px 20px;
        }

        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 35px;
            padding-bottom: 20px;
            border-bottom: 2px solid #f0f0f0;
            flex-wrap: wrap;
            gap: 20px;
        }

        .header-title {
            flex: 1;
        }

        .header-title h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 5px;
            font-weight: 600;
        }

        .header-title p {
            color: #666;
            font-size: 15px;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 8px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        .table-container {
            overflow-x: auto;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        thead th {
            padding: 15px;
            text-align: left;
            color: white;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        tbody tr {
            border-bottom: 1px solid #f0f0f0;
            transition: background-color 0.2s;
        }

        tbody tr:hover {
            background-color: #f8f9ff;
        }

        tbody td {
            padding: 15px;
            color: #333;
            font-size: 14px;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }

        .empty-state h3 {
            font-size: 20px;
            margin-bottom: 10px;
            color: #666;
        }

        .actions {
            display: flex;
            gap: 10px;
        }

        .action-link {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            font-size: 13px;
            transition: color 0.2s;
        }

        .action-link:hover {
            color: #764ba2;
            text-decoration: underline;
        }

        .action-link.delete {
            color: #e74c3c;
        }

        .action-link.delete:hover {
            color: #c0392b;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-top: 30px;
            padding-top: 25px;
            border-top: 2px solid #f0f0f0;
        }

        .pagination a,
        .pagination strong {
            padding: 8px 14px;
            border-radius: 6px;
            text-decoration: none;
            color: #667eea;
            font-weight: 500;
            transition: all 0.2s;
            border: 2px solid transparent;
        }

        .pagination a:hover {
            background-color: #f8f9ff;
            border-color: #667eea;
        }

        .pagination strong {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-color: #667eea;
        }

        .pagination-info {
            text-align: center;
            margin-top: 15px;
            color: #666;
            font-size: 14px;
        }

        .id-badge {
            display: inline-block;
            background: #e8f4fd;
            color: #0066cc;
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }

        .description-cell {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            .header {
                flex-direction: column;
                align-items: flex-start;
            }

            .header-title h1 {
                font-size: 24px;
            }

            table {
                font-size: 12px;
            }

            thead th,
            tbody td {
                padding: 10px 8px;
            }

            .description-cell {
                max-width: 150px;
            }

            .pagination {
                flex-wrap: wrap;
            }
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .container {
            animation: fadeIn 0.5s ease;
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
            font-size: 14px;
            border-left: 4px solid #28a745;
            display: flex;
            align-items: center;
        }

        .success-message::before {
            content: "✓";
            font-size: 20px;
            margin-right: 10px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div class="header-title">
            <h1>📋 Liste des annonces</h1>
            <p>Gérez vos annonces en toute simplicité</p>
        </div>
        <a href="${pageContext.request.contextPath}/annonce/add" class="btn btn-primary">
            ➕ Nouvelle annonce
        </a>
    </div>

    <%
        String success = (String) request.getAttribute("success");
        if (success != null && !success.isEmpty()) {
    %>
    <div class="success-message">
        <%= success %>
    </div>
    <%
        }
    %>

    <div class="table-container">
        <%
            PaginatedResponses paginatedResponse = (PaginatedResponses) request.getAttribute("paginatedAnnonces");
            List<Annonce> annonces = null;

            if (paginatedResponse != null) {
                annonces = (List<Annonce>) paginatedResponse.getItems();
            }

            if (annonces != null && !annonces.isEmpty()) {
        %>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Titre</th>
                <th>Description</th>
                <th>Adresse</th>
                <th>Email</th>
                <th>Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (Annonce a : annonces) {
            %>
            <tr>
                <td><span class="id-badge">#<%= a.getId() %></span></td>
                <td><strong><%= a.getTitle() %></strong></td>
                <td class="description-cell" title="<%= a.getDescription() %>">
                    <%= a.getDescription() %>
                </td>
                <td><%= a.getAdress() %></td>
                <td><%= a.getMail() %></td>
                <td><%= a.getDate() %></td>
                <td>
                    <div class="actions">
                        <a href="${pageContext.request.contextPath}/annonce/update?id=<%= a.getId() %>"
                           class="action-link">✏️ Modifier</a>
                        <a href="${pageContext.request.contextPath}/annonce/delete?id=<%= a.getId() %>"
                           class="action-link delete"
                           onclick="return confirm('Voulez-vous vraiment supprimer cette annonce ?');">
                            🗑️ Supprimer
                        </a>
                    </div>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <%
        } else {
        %>
        <div class="empty-state">
            <div class="empty-state-icon">📭</div>
            <h3>Aucune annonce trouvée</h3>
            <p>Commencez par créer votre première annonce !</p>
        </div>
        <%
            }
        %>
    </div>

    <!-- Pagination -->
    <%
        int currentPage = 1;
        int nbrPages = 1;

        if (paginatedResponse != null) {
            currentPage = paginatedResponse.getCurrentPage();
            nbrPages = paginatedResponse.getTotalPages();
        }

        AnnonceSearchParams params = (AnnonceSearchParams) request.getAttribute("params");

        String queryParams = "";
        int size = 5;

        if (params != null) {
            size = params.getSize() > 0 ? params.getSize() : 5;

            if (params.getKeyword() != null && !params.getKeyword().isEmpty()) {
                queryParams += "&keyword=" + java.net.URLEncoder.encode(params.getKeyword(), "UTF-8");
            }
            if (params.getStatus() != null) {
                queryParams += "&status=" + params.getStatus();
            }
            if (params.getCategoryId() != null) {
                queryParams += "&categoryId=" + params.getCategoryId();
            }
        }

        if (nbrPages > 1) {
    %>
    <div class="pagination">
        <%
            if (currentPage > 1) {
        %>
        <a href="?page=<%= currentPage - 1 %>&size=<%= size %><%= queryParams %>">← Précédent</a>
        <%
            }

            for (int i = 1; i <= nbrPages; i++) {
                if (i == currentPage) {
        %>
        <strong><%= i %></strong>
        <%
        } else {
        %>
        <a href="?page=<%= i %>&size=<%= size %><%= queryParams %>"><%= i %></a>
        <%
                }
            }

            if (currentPage < nbrPages) {
        %>
        <a href="?page=<%= currentPage + 1 %>&size=<%= size %><%= queryParams %>">Suivant →</a>
        <%
            }
        %>
    </div>

    <div class="pagination-info">
        Page <%= currentPage %> sur <%= nbrPages %>
    </div>
    <%
        }
    %>
</div>

</body>
</html>
