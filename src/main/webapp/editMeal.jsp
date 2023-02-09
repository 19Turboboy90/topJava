<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<section>
    <h3><a href="index.html" class="header">Home</a></h3>
    <hr>
    <h2>${param.action == 'create' ? 'Add Meal' : 'Update'}</h2>
    <form method="post" action='meals'>
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime :</dt>
            <dd><input type="datetime-local" name="dateTime" value="${meal.dateTime}"/> <br/></dd>
        </dl>

        <dl>
            <dt>Description :</dt>
            <dd><input type="text" name="description" value="${meal.description}"/> <br/></dd>
        </dl>

        <dl>
            <dt>Calories :</dt>
            <dd><input type="number" name="calories" value="${meal.calories}"/> <br/></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
</body>
</html>