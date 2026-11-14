<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="home"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<h2>Book: <c:out value="${item.name}"/></h2>
<p class="sub"><c:out value="${item.description}"/></p>

<form class="panel form" method="post" action="/book/${item.id}">
    <p class="rate">&#8377;<fmt:formatNumber value="${item.dailyRate}" pattern="#,##0"/> <small>/ day</small></p>

    <label>Start date
        <input id="startDate" name="startDate" type="date" value="${today}" min="${today}" onchange="showRent()" required>
    </label>
    <label>Due date (when you will return it)
        <input id="dueDate" name="dueDate" type="date" min="${today}" onchange="showRent()" required>
    </label>

    <p id="rentText" class="estimate">Pick the dates to see the rent.</p>
    <p class="hint">Returning after the due date adds a late fee of 1.5 x daily rent for every late day.</p>

    <div class="row">
        <button class="btn">Confirm booking</button>
        <a class="btn light" href="/">Cancel</a>
    </div>
</form>

<script>
    // shows the rent on the page before the form is sent
    // (the real amount is calculated again on the server)
    var dailyRate = ${item.dailyRate};

    function showRent() {
        // the due date picker should not allow a day before the start date
        document.getElementById("dueDate").min = document.getElementById("startDate").value;

        var start = new Date(document.getElementById("startDate").value);
        var due = new Date(document.getElementById("dueDate").value);
        var days = Math.round((due - start) / (1000 * 60 * 60 * 24));
        var text = document.getElementById("rentText");

        if (isNaN(days)) {
            text.innerText = "Pick the dates to see the rent.";
        } else if (days < 0) {
            text.innerText = "Due date cannot be before the start date.";
        } else {
            if (days == 0) {
                days = 1;
            }
            // the rupee sign is written with its unicode number 20B9
            text.innerText = days + " day(s) x \u20B9" + dailyRate + " = \u20B9" + (days * dailyRate);
        }
    }
</script>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
