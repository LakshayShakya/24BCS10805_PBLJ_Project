<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="current" value="login"/>
<%@ include file="/WEB-INF/views/common/userHeader.jspf" %>

<form class="panel form auth" method="post" action="/login">
    <h2>Login</h2>
    <p class="sub">Users and the admin log in from the same page.</p>

    <label>Email
        <input name="email" type="email" required autofocus>
    </label>
    <label>Password
        <input name="password" type="password" required>
    </label>
    <button class="btn">Login</button>

    <p class="hint">New here? <a href="/register">Create an account</a></p>
    <p class="hint demo">
        Demo admin: admin@renttrack.com / admin123<br>
        Demo user: user@renttrack.com / user123
    </p>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
