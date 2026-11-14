package com.renttrack.controller;

import com.renttrack.model.Equipment;
import com.renttrack.service.EquipmentService;
import com.renttrack.service.RentalService;
import com.renttrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Admin dashboard and the list of registered users.
@Controller
public class AdminController {

    private final EquipmentService equipmentService;
    private final RentalService rentalService;
    private final UserService userService;

    public AdminController(EquipmentService equipmentService, RentalService rentalService,
                           UserService userService) {
        this.equipmentService = equipmentService;
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("totalItems", equipmentService.countAll());
        model.addAttribute("availableCount", equipmentService.countByStatus(Equipment.AVAILABLE));
        model.addAttribute("rentedCount", equipmentService.countByStatus(Equipment.RENTED));
        model.addAttribute("repairCount", equipmentService.countByStatus(Equipment.REPAIR));
        model.addAttribute("overdueList", rentalService.getOverdueRentals());
        model.addAttribute("collected", rentalService.getTotalCollected());
        return "admin/dashboard";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getCustomers());
        return "admin/users";
    }
}
