<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html" class="header">Home</a></h3>
<hr>
<section>
    <h2>Meals</h2>
    <p><a href="meals?action=create">Add Meal</a></p>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Update</th>
            <th>Delete</th>
        </tr>
        <c:forEach var="meal" items="${requestScope.meals}">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr style="color:${meal.excess ? 'red' : 'green'}">
                <td>
                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                                   type="both"/>
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a href="meals?action=update&id=${meal.id}">
                        <img src="img/update.png">
                    </a></td>
                <td>
                    <a href="meals?action=delete&id=${meal.id}">
                        <img src="img/delete.png"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>