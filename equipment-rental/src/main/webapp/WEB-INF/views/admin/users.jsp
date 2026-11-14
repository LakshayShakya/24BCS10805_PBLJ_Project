<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="users"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Users</h2>
<p class="sub">People who have created an account on the website.</p>

<c:choose>
    <c:when test="${empty users}">
        <p class="empty">Nobody has signed up yet.</p>
    </c:when>
    <c:otherwise>
        <div class="panel">
            <table>
                <tr><th>Name</th><th>Email</th><th>Phone</th></tr>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td><c:out value="${u.fullName}"/></td>
                        <td><c:out value="${u.email}"/></td>
                        <td><c:out value="${u.phone}"/></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
