<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="damage"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Damage Reports</h2>
<p class="sub">A damaged item cannot be booked until it is marked as repaired here.</p>

<c:choose>
    <c:when test="${empty reports}">
        <p class="empty">No damage has been reported.</p>
    </c:when>
    <c:otherwise>
        <div class="panel">
            <table>
                <tr><th>Item</th><th>Returned by</th><th>Problem</th><th>Charge</th><th>Date</th><th>Status</th></tr>
                <c:forEach var="d" items="${reports}">
                    <tr>
                        <td><c:out value="${d.equipment.name}"/></td>
                        <td><c:out value="${d.customerName}"/></td>
                        <td class="wrap"><c:out value="${d.description}"/></td>
                        <td>&#8377;<fmt:formatNumber value="${d.repairCharge}" pattern="#,##0"/></td>
                        <td>${d.reportedOn}</td>
                        <td>
                            <c:choose>
                                <c:when test="${d.repaired}">
                                    <span class="badge green">Repaired</span>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="/admin/damage/${d.id}/repaired">
                                        <button class="btn small">Mark repaired</button>
                                    </form>
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
