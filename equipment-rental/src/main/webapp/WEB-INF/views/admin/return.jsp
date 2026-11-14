<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="rentals"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Return Item</h2>
<p class="sub">
    <c:out value="${rental.equipment.name}"/> taken by <c:out value="${rental.customerName}"/>
    from ${rental.startDate}, due on ${rental.dueDate}.
</p>

<div class="stats">
    <div class="stat"><b>&#8377;<fmt:formatNumber value="${rental.rentAmount}" pattern="#,##0"/></b>Rent</div>
    <div class="stat ${rental.lateDays > 0 ? 'red' : 'green'}"><b>${rental.lateDays}</b>Late days (till today)</div>
    <div class="stat"><b>&#8377;<fmt:formatNumber value="${lateFeeToday}" pattern="#,##0"/></b>Late fee if returned today</div>
</div>

<form class="panel form" method="post" action="/admin/rentals/${rental.id}/return">
    <label>Actual return date
        <input name="returnDate" type="date" value="${today}" min="${rental.startDate}" max="${today}" required>
    </label>

    <label class="check">
        <input type="checkbox" name="damaged" value="true"> Item came back damaged
    </label>

    <%-- these two boxes are used only when the checkbox above is ticked --%>
    <label>What is damaged?
        <input name="damageNote" placeholder="e.g. Lens scratched, charger missing">
    </label>
    <label>Repair charge (&#8377;)
        <input name="repairCharge" type="number" min="0" value="0">
    </label>

    <p class="hint">The late fee is calculated from the return date you pick above.</p>
    <div class="row">
        <button class="btn">Complete return</button>
        <a class="btn light" href="/admin/rentals">Cancel</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
