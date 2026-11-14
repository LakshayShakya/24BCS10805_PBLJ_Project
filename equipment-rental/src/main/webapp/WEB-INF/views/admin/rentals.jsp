<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="rentals"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Rentals</h2>
<p class="sub">Bookings made by the users. Press "Return" when an item comes back.</p>

<c:choose>
    <c:when test="${empty rentals}">
        <p class="empty">No bookings yet.</p>
    </c:when>
    <c:otherwise>
        <div class="panel">
            <table>
                <tr>
                    <th>Item</th><th>User</th><th>From</th><th>Due</th><th>Returned</th>
                    <th>Rent</th><th>Late fee</th><th>Damage</th><th>Total</th><th>Status</th>
                </tr>
                <c:forEach var="r" items="${rentals}">
                    <tr>
                        <td><c:out value="${r.equipment.name}"/></td>
                        <td><c:out value="${r.customerName}"/><br><small><c:out value="${r.phone}"/></small></td>
                        <td>${r.startDate}</td>
                        <td>${r.dueDate}</td>
                        <td>${r.returned ? r.returnDate : '-'}</td>
                        <td>&#8377;<fmt:formatNumber value="${r.rentAmount}" pattern="#,##0"/></td>
                        <td>&#8377;<fmt:formatNumber value="${r.lateFee}" pattern="#,##0"/></td>
                        <td>&#8377;<fmt:formatNumber value="${r.damageCharge}" pattern="#,##0"/></td>
                        <td><b>&#8377;<fmt:formatNumber value="${r.total}" pattern="#,##0"/></b></td>
                        <td>
                            <c:choose>
                                <c:when test="${r.returned}">
                                    <span class="badge green">Closed</span>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${r.lateDays > 0}">
                                        <span class="badge red">Late ${r.lateDays}d</span>
                                    </c:if>
                                    <a class="btn small" href="/admin/rentals/${r.id}/return">Return</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
