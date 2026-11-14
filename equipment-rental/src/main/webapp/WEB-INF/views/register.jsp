<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="register"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<form class="panel form auth" method="post" action="/register">
    <h2>Create account</h2>
    <p class="sub">You need an account to book equipment.</p>

    <label>Full name
        <input name="fullName" required autofocus>
    </label>
    <label>Email
        <input name="email" type="email" required>
    </label>
    <label>Phone number
        <input name="phone" pattern="[0-9]{10}" title="Enter a 10 digit number" required>
    </label>
    <label>Password (at least 6 characters)
        <input name="password" type="password" minlength="6" required>
    </label>
    <button class="btn">Sign up</button>

    <p class="hint">Already have an account? <a href="/login">Login</a></p>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
