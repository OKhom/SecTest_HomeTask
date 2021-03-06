<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
<div align="center">
    <c:url value="/newuser" var="regUrl" />

    <form action="${regUrl}" method="POST">
        Login:<br/><input type="text" name="login" value="${login}"><br/>
        Password:<br/><input type="password" name="password"><br/>
        E-mail:<br/><input type="text" name="email"><br/>
        Phone:<br/><input type="text" name="phone"><br/>
        Age:<br/><input type="number" name="age"><br/>
        <input type="submit" />

        <c:if test="${error eq 'exist'}">
            <p>User already exists!</p>
        </c:if>

        <c:if test="${error eq 'noemail'}">
            <p>You need to enter e-mail!</p>
        </c:if>
    </form>
</div>
</body>
</html>
