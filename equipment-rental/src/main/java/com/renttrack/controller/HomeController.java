package com.renttrack.controller;

import com.renttrack.model.User;
import com.renttrack.service.EquipmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Public home page: anyone can browse the equipment.
@Controller
public class HomeController {

    private final EquipmentService equipmentService;

    public HomeController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "All") String category,
                       @RequestParam(defaultValue = "") String search,
                       HttpSession session, Model model) {

        // the admin has his own panel
        User user = (User) session.getAttribute("loggedUser");
        if (user != null && user.isAdmin()) {
            return "redirect:/admin";
        }

        model.addAttribute("items", equipmentService.findItems(category, search));
        model.addAttribute("category", category);
        model.addAttribute("search", search);
        return "home";
    }
}
