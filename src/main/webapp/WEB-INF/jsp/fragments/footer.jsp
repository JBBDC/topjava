<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--https://getbootstrap.com/docs/4.0/examples/sticky-footer/--%>
<footer class="footer">
    <div class="container">
        <span class="text-muted"><spring:message code="app.footer"/></span>
    </div>
</footer>
<script type="text/javascript">
    const i18n = [];
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus",
    "common.confirm","meal.title","meal.edit","meal.add","meal.filter","meal.startDate","meal.endDate","meal.dateTime","meal.calories",
    "meal.description","user.title","user.edit","user.add","user.name","user.email","user.roles","user.active","user.registered","user.password"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>