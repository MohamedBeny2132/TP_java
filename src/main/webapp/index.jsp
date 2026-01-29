<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Formulaire</title>
</head>
<body>

<h1>Hello World!</h1>

<form method="post" action="annonce-servlet">
    <label>
        Title :
        <input type="text" name="title" required>
    </label>
    <br><br>

    <label>
        Description :
        <input type="text" name="description" required>
    </label>
    <br><br>
    <label>
        adress :
        <input type="text" name="adress" required>
    </label>
    <br><br>
    <label>
        Mail :
        <input type="text" name="description" required>
    </label>
    <br><br>

    <button type="submit">Envoyer</button>
</form>

</body>
</html>
