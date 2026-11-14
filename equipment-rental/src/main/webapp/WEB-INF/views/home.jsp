<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="home"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<section class="hero">
    <div class="sticker">Pay only<br>per day</div>
    <h2>Need a camera for the weekend? Don't buy it, rent it.</h2>
    <p>Cameras, laptops, projectors and tools on daily rent. Book it here and pick it up from our shop.</p>
</section>

<div class="steps">
    <div class="step"><b>1.</b> Choose an item from the list below</div>
    <div class="step"><b>2.</b> Log in and select your dates</div>
    <div class="step"><b>3.</b> Collect it, use it, return it on time</div>
</div>

<h3>Our equipment</h3>

<%-- search and category filter --%>
<form class="row filters" method="get" action="/">
    <input name="search" value="<c:out value='${search}'/>" placeholder="Search by name">
    <select name="category">
        <c:forEach var="cat" items="All,Camera,Laptop,Projector,Tool">
            <option ${cat == category ? 'selected' : ''}>${cat}</option>
        </c:forEach>
    </select>
    <button class="btn">Search</button>
</form>

<div class="grid">
    <c:forEach var="item" items="${items}">
        <div class="card">
            <span class="category"><c:out value="${item.category}"/></span>
            <h4><c:out value="${item.name}"/></h4>
            <p class="desc"><c:out value="${item.description}"/></p>
            <p class="rate">&#8377;<fmt:formatNumber value="${item.dailyRate}" pattern="#,##0"/> <small>/ day</small></p>

            <c:choose>
                <c:when test="${item.status == 'AVAILABLE'}">
                    <span class="badge green">Available</span>
                    <a class="btn small" href="/book/${item.id}">Book now</a>
                </c:when>
                <c:when test="${item.status == 'RENTED'}">
                    <span class="badge blue">Rented out</span>
                </c:when>
                <c:otherwise>
                    <span class="badge orange">Under repair</span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:forEach>
</div>

<c:if test="${empty items}">
    <p class="empty">No item matches your search.</p>
</c:if>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
