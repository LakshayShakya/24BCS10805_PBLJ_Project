<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="dashboard"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Dashboard</h2>
<p class="sub">Quick view of what is in the shop and what is out on rent.</p>

<div class="stats">
    <div class="stat"><b>${totalItems}</b>Total items</div>
    <div class="stat green"><b>${availableCount}</b>Available</div>
    <div class="stat blue"><b>${rentedCount}</b>Rented out</div>
    <div class="stat orange"><b>${repairCount}</b>Under repair</div>
    <div class="stat red"><b>${overdueList.size()}</b>Overdue</div>
    <div class="stat"><b>&#8377;<fmt:formatNumber value="${collected}" pattern="#,##0"/></b>Collected so far</div>
</div>

<h3>Overdue rentals</h3>
<c:choose>
    <c:when test="${empty overdueList}">
        <p class="empty">Nothing is overdue. All good!</p>
    </c:when>
    <c:otherwise>
        <div class="panel">
            <table>
                <tr><th>Item</th><th>User</th><th>Phone</th><th>Due date</th><th>Late by</th><th></th></tr>
                <c:forEach var="r" items="${overdueList}">
                    <tr>
                        <td><c:out value="${r.equipment.name}"/></td>
                        <td><c:out value="${r.customerName}"/></td>
                        <td><c:out value="${r.phone}"/></td>
                        <td>${r.dueDate}</td>
                        <td><span class="badge red">${r.lateDays} days</span></td>
                        <td><a class="btn small" href="/admin/rentals/${r.id}/return">Return</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
