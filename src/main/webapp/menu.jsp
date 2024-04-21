<%@ page import="com.example.model.Menu" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: tymofii
  Date: 4/19/24
  Time: 4:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- menu.jsp -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Menu</title>
</head>
<body>
<%
    if (request.getAttribute("menus") != null) {
        List<Menu> menus = (List<Menu>) request.getAttribute("menus");
    }
%>
<h1>Menu</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Meal/Drink</th>
        <th>Cost</th>
    </tr>
    <c:forEach var="menu" items="${menus}">
        <tr>
            <td>${menu.id}</td>
            <td>${menu.name}</td>
            <td>${menu.meal ? 'Meal' : 'Drink'}</td>
            <td>${menu.cost}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>