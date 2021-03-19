<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>
</head>
<body>
<div align="center">
    <h1>Secret page for admins only!</h1>

    <p>Click to go back: <a href="/">back</a></p>

    <c:url value="/logout" var="logoutUrl" />
    <p>Click to logout: <a href="${logoutUrl}">LOGOUT</a></p>

    <button type="button" id="add_user">Add</button>
    <button type="button" id="delete_user">Delete</button>

    <table border="1">
        <c:forEach items="${users}" var="user">
            <tr>
                <td><input type="checkbox" name="IDs" value="${user.id}" id="check_${user.id}"></td>
                <td><c:out value="${user.login}"/></td>
                <td><c:out value="${user.role}"/></td>
            </tr>
        </c:forEach>
    </table>
    <button type="button" id="update_role">Update Users Role</button>
    <select name="new_role" id="new_role">
        <c:forEach var="r" items="${roles}">
            <option value=${r}>ROLE_${r}</option>
        </c:forEach>
    </select>

    <c:set var="role" value="${role}"/>
    <c:if test="${not fn:contains(role, 'ROLE_ADMIN')}">
        <p>You haven't permission for delete!</p>
    </c:if>
</div>

<script>
    $('#add_user').click(function(){
        window.location.href = "/register";
    });

    $('#delete_user').click(function(){
        var data = { 'toDelete' : []};
        $('input[name="IDs"]:checked').each(function() {
            data['toDelete'].push($(this).val());
        });
        $.post("/delete", data, function(data, status) {
            window.location.reload();
        });
    });

    $('#update_role').click(function(){
        var data = {'ids' : [],
        'newRole' : $("#new_role").val()};
        $('input[name="IDs"]:checked').each(function() {
            data['ids'].push($(this).val());
        });
        // alert("IDs: " + data['ids'].toString() + "\nNew Role: " + data['newRole']);
        $.post("/role/update", data, function(data, status) {
            window.location.reload();
        });
    });
</script>

</body>
</html>
