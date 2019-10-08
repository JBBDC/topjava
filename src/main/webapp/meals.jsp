<%--  Created by IntelliJ IDEA.--%>
<%--  User: Leontev--%>
<%--  Date: 04.10.2019--%>
<%--  Time: 17:21--%>
<%--  To change this template use File | Settings | File Templates.--%>

<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");%>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3>
    <table align="center" cellpadding="5" cellspacing="5">

        <tr align="center">
            <td>Дата/Время</td>
            <td>Описание</td>
            <td>Калории</td>
        </tr>

        <c:forEach var="meal" items="${meals}">

            <c:set var="textColor" value="${meal.exceed == true ? 'red' : 'green'}"/>

            <tr style=color:${textColor}>
                <c:set var="mealId" value="${meal.id}"/>
                <c:set var="meal" value="${meal}"/>
                <c:set var="isEdit" value="false"/>
                <% MealTo meal = (MealTo) pageContext.getAttribute("meal");
                    String date = formatter.format((meal.getDateTime()));
                    Long editId = 100L;
                    Long mealId = 200L;
                    if (request.getAttribute("editRow") != null) {
                        editId = (Long) request.getAttribute("editRow");
                        mealId = (Long) pageContext.getAttribute("mealId");
                    }
                    if (mealId.equals(editId)) {%>
                <form method="post">
                    <td align="center"><input type="datetime-local" name="dateTime" value="<%=meal.getDateTime()%>"></td>
                    <td align="center"><input type="text" name="description" value="${meal.description}"></td>
                    <td align="center"><input type="text" name="calories" value="${meal.calories}"></td>
                    <td>
                        <input type="hidden" name="action" value="save">
                        <input type="hidden" name="id" value="${meal.id}">
                        <input type="submit" value="Сохранить">
                    </td>
                </form>
                <%
                } else {
                %>
                <form method="post">
                <td align="center"><%=date%></td>
                <td align="center">${meal.description}</td>
                <td align="center">${meal.calories}</td>
                    <td>
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="id" value="${meal.id}">
                        <input type="submit" value="Изменить">
                    </td>
                </form>
                <%
                    }
                %>
                <form method="post">
                    <td>
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${meal.id}">
                        <input type="submit" value="Удалить">
                    </td>
                </form>
            </tr>
        </c:forEach>

        <tr>
            <form method="post">
                <input type="hidden" name="action" value="create">
                <td align="center"><<input type="datetime-local" name="dateTime"></td>
                <td align="center"><input type="text" name="description"></td>
                <td align="center"><input type="text" name="calories"></td>
                <td align="center"><input type="submit" name="Добавить"/></td>
            </form>
        </tr>


    </table>
</h3>
</body>
</html>