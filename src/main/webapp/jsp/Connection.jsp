<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Connexion</title>
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      padding: 20px;
    }

    .login-container {
      background: white;
      padding: 40px;
      border-radius: 10px;
      box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
      width: 100%;
      max-width: 400px;
    }

    .login-header {
      text-align: center;
      margin-bottom: 30px;
    }

    .login-header h1 {
      color: #333;
      font-size: 28px;
      margin-bottom: 10px;
    }

    .login-header p {
      color: #666;
      font-size: 14px;
    }

    .form-group {
      margin-bottom: 20px;
    }

    .form-group label {
      display: block;
      margin-bottom: 8px;
      color: #333;
      font-weight: 500;
      font-size: 14px;
    }

    .form-group input {
      width: 100%;
      padding: 12px 15px;
      border: 1px solid #ddd;
      border-radius: 5px;
      font-size: 14px;
      transition: border-color 0.3s;
    }

    .form-group input:focus {
      outline: none;
      border-color: #667eea;
    }

    .error-message {
      background-color: #fee;
      color: #c33;
      padding: 12px;
      border-radius: 5px;
      margin-bottom: 20px;
      font-size: 14px;
      border-left: 4px solid #c33;
    }

    .success-message {
      background-color: #efe;
      color: #3c3;
      padding: 12px;
      border-radius: 5px;
      margin-bottom: 20px;
      font-size: 14px;
      border-left: 4px solid #3c3;
    }

    .btn-login {
      width: 100%;
      padding: 12px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      border-radius: 5px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s;
    }

    .btn-login:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
    }

    .btn-login:active {
      transform: translateY(0);
    }

    .form-footer {
      margin-top: 20px;
      text-align: center;
    }

    .form-footer a {
      color: #667eea;
      text-decoration: none;
      font-size: 14px;
    }

    .form-footer a:hover {
      text-decoration: underline;
    }

    .divider {
      text-align: center;
      margin: 20px 0;
      color: #999;
      font-size: 14px;
    }
  </style>
</head>
<body>

<div class="login-container">
  <div class="login-header">
    <h1>🔐 Connexion</h1>
    <p>Connectez-vous à votre compte</p>
  </div>

  <%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");

    if (error != null && !error.isEmpty()) {
  %>
  <div class="error-message">
    <%= error %>
  </div>
  <%
    }

    if (success != null && !success.isEmpty()) {
  %>
  <div class="success-message">
    <%= success %>
  </div>
  <%
    }
  %>

  <form action="${pageContext.request.contextPath}/login" method="post">
    <div class="form-group">
      <label for="email">Email</label>
      <input
              type="email"
              id="email"
              name="email"
              placeholder="exemple@email.com"
              value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>"
              required>
    </div>

    <div class="form-group">
      <label for="password">Mot de passe</label>
      <input
              type="password"
              id="password"
              name="password"
              placeholder="••••••••"
              required>
    </div>

    <button type="submit" class="btn-login">Se connecter</button>
  </form>

  <div class="form-footer">
    <a href="${pageContext.request.contextPath}/forgot-password">Mot de passe oublié ?</a>
  </div>

  <div class="divider">ou</div>

  <div class="form-footer">
    <p style="color: #666; font-size: 14px;">
      Pas encore de compte ?
      <a href="${pageContext.request.contextPath}/register">S'inscrire</a>
    </p>
  </div>
</div>

</body>
</html>