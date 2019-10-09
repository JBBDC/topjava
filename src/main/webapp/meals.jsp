<%--  Created by IntelliJ IDEA.--%>
<%--  User: Leontev--%>
<%--  Date: 04.10.2019--%>
<%--  Time: 17:21--%>
<%--  To change this template use File | Settings | File Templates.--%>

<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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

            <c:set var="textColor" value="${meal.exceed? 'red' : 'green'}"/>

            <tr style=color:${textColor}>
                <c:set var="meal" value="${meal}"/>
                <c:set var="editRow" value="${editRow}"/>
                <c:set var="formatter" value="${formatter}"/>

                <form method="post">
                    <td align="center">${editRow == meal.id
                            ?'<input type="datetime-local" name="dateTime" value="'+=meal.dateTime+='">'
                            :formatter.format(meal.dateTime)}
                    </td>
                    <td align="center">${editRow == meal.id
                            ?'<input type="text" name="description" value="'+=meal.description+='">'
                            :meal.description}
                    </td>
                    <td align="center">${editRow == meal.id
                            ?'<input type="text" name="calories" value="'+=meal.calories+='">'
                            :meal.calories}
                    </td>
                    <td>
                        <input type="hidden" name="action" value="${editRow == meal.id ? 'save' : 'edit'}">
                        <input type="hidden" name="id" value="${meal.id}">
                        <input type="submit" value="${editRow == meal.id ? 'Сохранить' : 'Изменить'}">
                    </td>
                </form>
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