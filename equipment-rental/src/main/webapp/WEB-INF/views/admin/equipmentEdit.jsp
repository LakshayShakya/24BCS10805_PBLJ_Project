<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="equipment"/>
<%@ include file="/WEB-INF/views/common/adminHeader.jspf" %>

<h2>Update item</h2>
<p class="sub">Change the details and press save.</p>

<form class="panel form" method="post" action="/admin/equipment/${item.id}/edit">
    <label>Item name
        <input name="name" value="<c:out value='${item.name}'/>" required>
    </label>
    <label>Category
        <select name="category">
            <c:forEach var="cat" items="Camera,Laptop,Projector,Tool">
                <option ${cat == item.category ? 'selected' : ''}>${cat}</option>
            </c:forEach>
        </select>
    </label>
    <label>Description
        <input name="description" value="<c:out value='${item.description}'/>">
    </label>
    <label>Rent per day (&#8377;)
        <input name="dailyRate" type="number" min="1" value="<fmt:formatNumber value='${item.dailyRate}' pattern='0'/>" required>
    </label>
    <div class="row">
        <button class="btn">Save changes</button>
        <a class="btn light" href="/admin/equipment">Cancel</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
