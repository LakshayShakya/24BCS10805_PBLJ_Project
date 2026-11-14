package com.renttrack.config;

import com.renttrack.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

// Runs before the protected pages open and works like a gatekeeper:
//  - nobody logged in      -> go to the login page
//  - /admin pages          -> only for a user whose role is ADMIN
//  - booking pages         -> only for a normal user
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {

        User user = (User) request.getSession().getAttribute("loggedUser");
        String path = request.getRequestURI();

        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }
        if (path.startsWith("/admin") && !user.isAdmin()) {
            response.sendRedirect("/");
            return false;
        }
        // booking pages are for normal users, the admin goes to his panel
        if (!path.startsWith("/admin") && user.isAdmin()) {
            response.sendRedirect("/admin");
            return false;
        }
        return true; // allowed, continue to the controller
    }
}
