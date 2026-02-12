<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce" %>
<%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Status" %>
<%
    Annonce annonce = (Annonce) request.getAttribute("annonce");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détail de l'annonce - <%= annonce != null ? annonce.getTitle() : "" %></title>
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
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            overflow: hidden;
        }

        .header-image {
            height: 250px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        .header-image-icon {
            font-size: 80px;
            opacity: 0.3;
        }

        .back-button {
            position: absolute;
            top: 20px;
            left: 20px;
            background: rgba(255, 255, 255, 0.2);
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s;
        }

        .back-button:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateX(-5px);
        }

        .content {
            padding: 40px;
        }

        .title-section {
            margin-bottom: 30px;
            padding-bottom: 25px;
            border-bottom: 2px solid #f0f0f0;
        }

        .title-section h1 {
            color: #333;
            font-size: 36px;
            margin-bottom: 15px;
            line-height: 1.2;
        }

        .meta-info {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
        }

        .badge {
            display: inline-block;
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
        }

        .badge-id {
            background: #e8f4fd;
            color: #0066cc;
        }

        .badge-date {
            background: #f0f0f0;
            color: #666;
        }

        .badge-status {
            padding: 6px 14px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 600;
        }

        .status-draft {
            background: #ffeaa7;
            color: #d63031;
        }

        .status-published {
            background: #55efc4;
            color: #00b894;
        }

        .status-archived {
            background: #dfe6e9;
            color: #636e72;
        }

        .info-section {
            margin-bottom: 30px;
        }

        .info-section h2 {
            color: #333;
            font-size: 20px;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .info-section h2::before {
            content: "";
            width: 4px;
            height: 24px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 2px;
        }

        .description-box {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            line-height: 1.8;
            color: #555;
            font-size: 15px;
            border-left: 4px solid #667eea;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }

        .info-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            border-left: 4px solid #667eea;
        }

        .info-card-label {
            color: #999;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .info-card-value {
            color: #333;
            font-size: 16px;
            font-weight: 500;
            word-break: break-all;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            margin-top: 40px;
            padding-top: 30px;
            border-top: 2px solid #f0f0f0;
        }

        .btn {
            padding: 14px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            flex: 1;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-danger {
            background: white;
            color: #e74c3c;
            border: 2px solid #e74c3c;
        }

        .btn-danger:hover {
            background: #e74c3c;
            color: white;
            transform: translateY(-2px);
        }

        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }

        .btn-secondary:hover {
            background: #f8f9ff;
            transform: translateY(-2px);
        }

        .contact-highlight {
            background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
            padding: 25px;
            border-radius: 10px;
            margin-top: 30px;
            text-align: center;
        }

        .contact-highlight h3 {
            color: #333;
            font-size: 18px;
            margin-bottom: 15px;
        }

        .contact-email {
            display: inline-block;
            background: white;
            padding: 12px 24px;
            border-radius: 8px;
            color: #667eea;
            font-weight: 600;
            text-decoration: none;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            transition: all 0.3s;
        }

        .contact-email:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        }

        @media (max-width: 768px) {
            .content {
                padding: 25px;
            }

            .title-section h1 {
                font-size: 28px;
            }

            .info-grid {
                grid-template-columns: 1fr;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }

            .header-image {
                height: 180px;
            }

            .back-button {
                padding: 8px 16px;
                font-size: 14px;
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

        .error-message {
            background-color: #fee;
            color: #c33;
            padding: 20px;
            border-radius: 8px;
            margin: 20px;
            text-align: center;
            border-left: 4px solid #c33;
        }
    </style>
</head>
<body>

<%
    if (annonce == null) {
%>
<div class="container">
    <div class="error-message">
        <h2>❌ Annonce introuvable</h2>
        <p>L'annonce que vous recherchez n'existe pas ou a été supprimée.</p>
        <br>
        <a href="${pageContext.request.contextPath}/annonce/list" class="btn btn-primary">
            Retour à la liste
        </a>
    </div>
</div>
<%
} else {
%>

<div class="container">
    <div class="header-image">
        <a href="${pageContext.request.contextPath}/annonce/list" class="back-button">
            ← Retour
        </a>
        <div class="header-image-icon">📄</div>
    </div>

    <div class="content">
        <div class="title-section">
            <h1><%= annonce.getTitle() %></h1>
            <div class="meta-info">
                <span class="badge badge-id">ID #<%= annonce.getId() %></span>
                <span class="badge badge-date">
                    📅 <%= annonce.getDate() != null ? annonce.getDate() : "Date non spécifiée" %>
                </span>
                <%
                    if (annonce.getStatus() != null) {
                        String statusClass = "status-" + annonce.getStatus().toString().toLowerCase();
                        String statusText = "";
                        switch (annonce.getStatus()) {
                            case DRAFT:
                                statusText = "📝 Brouillon";
                                break;
                            case PUBLISHED:
                                statusText = "✅ Publiée";
                                break;
                            case ARCHIVED:
                                statusText = "📦 Archivée";
                                break;
                        }
                %>
                <span class="badge-status <%= statusClass %>"><%= statusText %></span>
                <%
                    }
                %>
            </div>
        </div>

        <div class="info-section">
            <h2>Description</h2>
            <div class="description-box">
                <%= annonce.getDescription() != null ? annonce.getDescription() : "Aucune description disponible" %>
            </div>
        </div>

        <div class="info-section">
            <h2>Informations</h2>
            <div class="info-grid">
                <div class="info-card">
                    <div class="info-card-label">📍 Adresse</div>
                    <div class="info-card-value">
                        <%= annonce.getAdress() != null ? annonce.getAdress() : "Non spécifiée" %>
                    </div>
                </div>
                <div class="info-card">
                    <div class="info-card-label">📧 Email de contact</div>
                    <div class="info-card-value">
                        <%= annonce.getMail() != null ? annonce.getMail() : "Non spécifié" %>
                    </div>
                </div>
            </div>
        </div>

        <div class="contact-highlight">
            <h3>💬 Intéressé par cette annonce ?</h3>
            <a href="mailto:<%= annonce.getMail() %>" class="contact-email">
                📧 Contacter par email
            </a>
        </div>

        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/annonce/update?id=<%= annonce.getId() %>"
               class="btn btn-primary">
                ✏️ Modifier l'annonce
            </a>
            <a href="${pageContext.request.contextPath}/annonce/list"
               class="btn btn-secondary">
                📋 Retour à la liste
            </a>
            <a href="${pageContext.request.contextPath}/annonce/delete?id=<%= annonce.getId() %>"
               class="btn btn-danger"
               onclick="return confirm('Voulez-vous vraiment supprimer cette annonce ?');">
                🗑️ Supprimer
            </a>
        </div>
    </div>
</div>

<%
    }
%>

</body>
</html>
