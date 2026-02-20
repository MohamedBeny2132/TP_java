<%@ page contentType="text/html; charset=UTF-8" %>
  <!DOCTYPE html>
  <html>

  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter une annonce</title>
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
        max-width: 700px;
        margin: 0 auto;
        background: white;
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
      }

      .header {
        text-align: center;
        margin-bottom: 35px;
        padding-bottom: 20px;
        border-bottom: 2px solid #f0f0f0;
      }

      .header h1 {
        color: #333;
        font-size: 32px;
        margin-bottom: 10px;
        font-weight: 600;
      }

      .header p {
        color: #666;
        font-size: 15px;
      }

      .error-message {
        background-color: #fee;
        color: #c33;
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 25px;
        font-size: 14px;
        border-left: 4px solid #c33;
        display: flex;
        align-items: center;
      }

      .error-message::before {
        content: "⚠";
        font-size: 20px;
        margin-right: 10px;
      }

      .success-message {
        background-color: #efe;
        color: #3c3;
        padding: 15px;
        border-radius: 8px;
        margin-bottom: 25px;
        font-size: 14px;
        border-left: 4px solid #3c3;
        display: flex;
        align-items: center;
      }

      .success-message::before {
        content: "✓";
        font-size: 20px;
        margin-right: 10px;
      }

      .form-group {
        margin-bottom: 25px;
      }

      .form-group label {
        display: block;
        margin-bottom: 8px;
        color: #333;
        font-weight: 500;
        font-size: 15px;
      }

      .form-group label .required {
        color: #e74c3c;
        margin-left: 3px;
      }

      .form-group input[type="text"],
      .form-group input[type="email"],
      .form-group textarea {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e0e0e0;
        border-radius: 8px;
        font-size: 15px;
        font-family: inherit;
        transition: all 0.3s ease;
        background-color: #fafafa;
      }

      .form-group input[type="text"]:focus,
      .form-group input[type="email"]:focus,
      .form-group textarea:focus {
        outline: none;
        border-color: #667eea;
        background-color: white;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
      }

      .form-group textarea {
        resize: vertical;
        min-height: 120px;
        line-height: 1.5;
      }

      .form-group input::placeholder,
      .form-group textarea::placeholder {
        color: #999;
      }

      .form-actions {
        display: flex;
        gap: 15px;
        margin-top: 35px;
        padding-top: 25px;
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

      .btn-primary:active {
        transform: translateY(0);
      }

      .btn-secondary {
        background: white;
        color: #667eea;
        border: 2px solid #667eea;
        flex: 0.5;
      }

      .btn-secondary:hover {
        background: #f8f9ff;
        transform: translateY(-2px);
      }

      .btn-secondary:active {
        transform: translateY(0);
      }

      .form-hint {
        font-size: 13px;
        color: #999;
        margin-top: 5px;
        font-style: italic;
      }

      @media (max-width: 600px) {
        .container {
          padding: 25px;
        }

        .header h1 {
          font-size: 26px;
        }

        .form-actions {
          flex-direction: column;
        }

        .btn {
          width: 100%;
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
    </style>
  </head>

  <body>

    <div class="container">
      <div class="header">
        <h1>📝 Nouvelle Annonce</h1>
        <p>Remplissez le formulaire pour publier votre annonce</p>
      </div>

      <% String error=(String) request.getAttribute("error"); String success=(String) request.getAttribute("success");
        if (error !=null && !error.isEmpty()) { %>
        <div class="error-message">
          <%= error %>
        </div>
        <% } if (success !=null && !success.isEmpty()) { %>
          <div class="success-message">
            <%= success %>
          </div>
          <% } %>

            <form method="post" action="${pageContext.request.contextPath}/annonce/add">
              <div class="form-group">
                <label for="title">
                  Titre
                  <span class="required">*</span>
                </label>
                <input type="text" id="title" name="title" placeholder="Ex: Appartement 2 pièces à louer"
                  value="<%= request.getParameter(" title") !=null ? request.getParameter("title") : "" %>"
                required>
                <p class="form-hint">Soyez clair et concis</p>
              </div>

              <div class="form-group">
                <label for="description">
                  Description
                  <span class="required">*</span>
                </label>
                <textarea id="description" name="description" placeholder="Décrivez votre annonce en détail..."
                  required><%= request.getParameter("description") != null ? request.getParameter("description") : "" %></textarea>
                <p class="form-hint">Minimum 20 caractères</p>
              </div>

              <div class="form-group">
                <label for="adress">
                  Adresse
                  <span class="required">*</span>
                </label>
                <input type="text" id="adress" name="adress" placeholder="Ex: 123 rue de Paris, 75001 Paris"
                  value="<%= request.getParameter(" adress") !=null ? request.getParameter("adress") : "" %>"
                required>
              </div>

              <div class="form-group">
                <label for="mail">
                  Email de contact
                  <span class="required">*</span>
                </label>
                <input type="email" id="mail" name="mail" placeholder="exemple@email.com"
                  value="<%= request.getParameter(" mail") !=null ? request.getParameter("mail") : "" %>"
                required>
                <p class="form-hint">Les intéressés pourront vous contacter à cette adresse</p>
              </div>

              <%@ page import="org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Category" %>
                <%@ page import="java.util.List" %>
                  <div class="form-group">
                    <label for="categoryId">
                      Catégorie
                      <span class="required">*</span>
                    </label>
                    <select id="categoryId" name="categoryId" required
                      style="width:100%;padding:12px 15px;border:2px solid #e0e0e0;border-radius:8px;font-size:15px;background:#fafafa;font-family:inherit;transition:all .3s ease;">
                      <option value="">-- Choisir une catégorie --</option>
                      <% List<Category> categories = (List<Category>) request.getAttribute("categories");
                          if (categories != null) {
                          for (Category cat : categories) {
                          %>
                          <option value="<%= cat.getId() %>">
                            <%= cat.getLabel() %>
                          </option>
                          <% } } %>
                    </select>
                  </div>

                  <button type="submit" class="btn btn-primary">
                    ✓ Publier l'annonce
                  </button>
                  <a href="${pageContext.request.contextPath}/annonce/list" class="btn btn-secondary">
                    Annuler
                  </a>
    </div>
    </form>
    </div>

  </body>

  </html>