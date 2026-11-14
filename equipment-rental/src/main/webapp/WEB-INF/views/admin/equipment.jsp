<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="equipment"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Equipment</h2>
<p class="sub">Add new items and update the details of existing ones.</p>

<%-- form to add a new item --%>
<form class="panel row" method="post" action="/admin/equipment/add">
    <input name="name" placeholder="Item name" required>
    <select name="category">
        <option>Camera</option>
        <option>Laptop</option>
        <option>Projector</option>
        <option>Tool</option>
    </select>
    <input name="description" placeholder="Short description">
    <input name="dailyRate" type="number" min="1" placeholder="Rent per day" required>
    <button class="btn">Add item</button>
</form>

<div class="panel gap">
    <table>
        <tr><th>Name</th><th>Category</th><th>Description</th><th>Rent / day</th><th>Status</th><th></th></tr>
        <c:forEach var="item" items="${items}">
            <tr>
                <td><b><c:out value="${item.name}"/></b></td>
                <td><c:out value="${item.category}"/></td>
                <td class="wrap"><c:out value="${item.description}"/></td>
                <td>&#8377;<fmt:formatNumber value="${item.dailyRate}" pattern="#,##0"/></td>
                <td>
                    <c:choose>
                        <c:when test="${item.status == 'AVAILABLE'}"><span class="badge green">Available</span></c:when>
                        <c:when test="${item.status == 'RENTED'}"><span class="badge blue">Rented out</span></c:when>
                        <c:otherwise><span class="badge orange">Under repair</span></c:otherwise>
                    </c:choose>
                </td>
                <td><a class="btn light small" href="/admin/equipment/${item.id}/edit">Edit</a></td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
