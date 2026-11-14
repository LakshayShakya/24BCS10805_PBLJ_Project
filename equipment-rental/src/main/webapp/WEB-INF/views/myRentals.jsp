<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="myRentals"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<h2>My Rentals</h2>
<p class="sub">Everything you have booked, with the bill after return.</p>

<c:choose>
    <c:when test="${empty rentals}">
        <p class="empty">You have not booked anything yet. <a href="/">Browse equipment</a></p>
    </c:when>
    <c:otherwise>
        <div class="panel">
            <table>
                <tr>
                    <th>Item</th><th>From</th><th>Due</th><th>Returned</th>
                    <th>Rent</th><th>Late fee</th><th>Damage</th><th>Total</th><th>Status</th>
                </tr>
                <c:forEach var="r" items="${rentals}">
                    <tr>
                        <td><c:out value="${r.equipment.name}"/></td>
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
                                    <span class="badge green">Returned</span>
                                </c:when>
                                <c:when test="${r.lateDays > 0}">
                                    <span class="badge red">Late by ${r.lateDays} days</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge blue">Active</span>
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
