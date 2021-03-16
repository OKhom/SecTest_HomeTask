<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
    <div align="center">
        <h1>Login activated page</h1>
        <c:url value="/activated" var="activateUrl" />
        <form action="${activateUrl}" method="POST">
            Login:<br/><input type="text" name="login" value="${login}" /><br/>
            Activated Code:<br/><input type="text" name="code" value="${code}" /><br/>
            <input type="submit" value="Activate" />

            <c:if test="${activation ne null}">
                <p>Entered activation code is incorrect!</p>
            </c:if>
        </form>
    </div>
</body>
</html>
