<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="error"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<%-- Spring Boot opens this page by itself when something goes wrong --%>
<h2>Something went wrong</h2>
<p class="sub">
    <c:choose>
        <c:when test="${status == 404}">This page does not exist.</c:when>
        <c:when test="${status == 400}">Some details in the form were missing or not correct. Please go back and try again.</c:when>
        <c:otherwise>The request could not be completed. Please try again.</c:otherwise>
    </c:choose>
</p>
<a class="btn" href="/">Back to home</a>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
