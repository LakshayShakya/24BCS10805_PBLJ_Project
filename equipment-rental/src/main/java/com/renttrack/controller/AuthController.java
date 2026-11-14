package com.renttrack.controller;

import com.renttrack.model.User;
import com.renttrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Login, sign up and logout.
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirect) {

        User user = userService.login(email, password);

        if (user == null) {
            redirect.addFlashAttribute("error", "Wrong email or password.");
            return "redirect:/login";
        }

        // remember the user until he logs out
        session.setAttribute("loggedUser", user);

        if (user.isAdmin()) {
            return "redirect:/admin";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String phone,
                           @RequestParam String password,
                           RedirectAttributes redirect) {

        String error = userService.register(fullName, email, phone, password);

        if (error != null) {
            redirect.addFlashAttribute("error", error);
            return "redirect:/register";
        }
        redirect.addFlashAttribute("message", "Account created. Please log in.");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
