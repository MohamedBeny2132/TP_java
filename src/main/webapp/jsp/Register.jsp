<%@ page contentType="text/html; charset=UTF-8" %>
  <!DOCTYPE html>
  <html lang="fr">

  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Créer un compte – MasterAnnonce</title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20px;
      }

      .card {
        background: white;
        border-radius: 18px;
        padding: 45px 40px;
        width: 100%;
        max-width: 480px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
        animation: slideUp 0.4s ease;
      }

      @keyframes slideUp {
        from {
          opacity: 0;
          transform: translateY(30px);
        }

        to {
          opacity: 1;
          transform: translateY(0);
        }
      }

      .logo {
        text-align: center;
        margin-bottom: 30px;
      }

      .logo h1 {
        font-size: 28px;
        font-weight: 700;
        color: #11998e;
      }

      .logo p {
        color: #888;
        font-size: 14px;
        margin-top: 6px;
      }

      .error-box {
        background: #fff0f0;
        border-left: 4px solid #e74c3c;
        color: #c0392b;
        padding: 12px 15px;
        border-radius: 8px;
        margin-bottom: 20px;
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 10px;
      }

      .form-group {
        margin-bottom: 20px;
      }

      .form-group label {
        display: block;
        font-size: 14px;
        font-weight: 600;
        color: #444;
        margin-bottom: 7px;
      }

      .form-group input {
        width: 100%;
        padding: 12px 15px;
        border: 2px solid #e8e8e8;
        border-radius: 10px;
        font-size: 15px;
        font-family: inherit;
        background: #fafafa;
        transition: all 0.25s ease;
      }

      .form-group input:focus {
        outline: none;
        border-color: #11998e;
        background: white;
        box-shadow: 0 0 0 3px rgba(17, 153, 142, 0.12);
      }

      .password-hint {
        font-size: 12px;
        color: #aaa;
        margin-top: 5px;
      }

      .btn-register {
        width: 100%;
        padding: 14px;
        background: linear-gradient(135deg, #11998e, #38ef7d);
        color: white;
        font-size: 16px;
        font-weight: 700;
        border: none;
        border-radius: 10px;
        cursor: pointer;
        margin-top: 10px;
        transition: all 0.25s ease;
        letter-spacing: 0.5px;
      }

      .btn-register:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(17, 153, 142, 0.35);
      }

      .divider {
        text-align: center;
        margin: 22px 0 18px;
        color: #bbb;
        font-size: 13px;
        position: relative;
      }

      .divider::before,
      .divider::after {
        content: '';
        position: absolute;
        top: 50%;
        width: 40%;
        height: 1px;
        background: #eee;
      }

      .divider::before {
        left: 0;
      }

      .divider::after {
        right: 0;
      }

      .login-link {
        text-align: center;
        font-size: 14px;
        color: #888;
      }

      .login-link a {
        color: #11998e;
        font-weight: 600;
        text-decoration: none;
      }

      .login-link a:hover {
        text-decoration: underline;
      }
    </style>
  </head>

  <body>

    <div class="card">

      <div class="logo">
        <h1>🏡 MasterAnnonce</h1>
        <p>Créez votre compte gratuitement</p>
      </div>

      <% String error=(String) request.getAttribute("error"); String username=(String) request.getAttribute("username");
        String email=(String) request.getAttribute("email"); if (username==null) username="" ; if (email==null) email=""
        ; if (error !=null && !error.isEmpty()) { %>
        <div class="error-box">⚠ <%= error %>
        </div>
        <% } %>

          <form method="post" action="${pageContext.request.contextPath}/register">

            <div class="form-group">
              <label for="username">Nom d'utilisateur</label>
              <input type="text" id="username" name="username" placeholder="ex: motez_ben" value="<%= username %>"
                required autofocus>
            </div>

            <div class="form-group">
              <label for="email">Adresse email</label>
              <input type="email" id="email" name="email" placeholder="exemple@mail.com" value="<%= email %>" required>
            </div>

            <div class="form-group">
              <label for="password">Mot de passe</label>
              <input type="password" id="password" name="password" placeholder="••••••••" required>
              <p class="password-hint">Minimum 6 caractères</p>
            </div>

            <div class="form-group">
              <label for="confirm">Confirmer le mot de passe</label>
              <input type="password" id="confirm" name="confirm" placeholder="••••••••" required>
            </div>

            <button type="submit" class="btn-register">Créer mon compte →</button>

          </form>

          <div class="divider">ou</div>

          <div class="login-link">
            Déjà un compte ?
            <a href="${pageContext.request.contextPath}/login">Se connecter</a>
          </div>

    </div>

  </body>

  </html>