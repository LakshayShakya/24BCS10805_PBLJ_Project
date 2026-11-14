package com.renttrack.controller;

import com.renttrack.model.Rental;
import com.renttrack.service.DamageService;
import com.renttrack.service.RentalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

// Admin: all rentals, taking an item back, and the damage reports.
@Controller
public class AdminRentalController {

    private final RentalService rentalService;
    private final DamageService damageService;

    public AdminRentalController(RentalService rentalService, DamageService damageService) {
        this.rentalService = rentalService;
        this.damageService = damageService;
    }

    @GetMapping("/admin/rentals")
    public String rentals(Model model) {
        model.addAttribute("rentals", rentalService.getAllRentals());
        return "admin/rentals";
    }

    // ---------- return ----------

    @GetMapping("/admin/rentals/{id}/return")
    public String returnForm(@PathVariable String id, Model model) {
        Rental rental = rentalService.getRental(id);
        if (rental == null || rental.isReturned()) {
            return "redirect:/admin/rentals";
        }
        model.addAttribute("rental", rental);
        model.addAttribute("today", LocalDate.now());
        // what the late fee would be if the item is returned today
        model.addAttribute("lateFeeToday", rentalService.calculateLateFee(rental));
        return "admin/return";
    }

    @PostMapping("/admin/rentals/{id}/return")
    public String saveReturn(@PathVariable String id,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                             @RequestParam(defaultValue = "false") boolean damaged,
                             @RequestParam(defaultValue = "") String damageNote,
                             @RequestParam(defaultValue = "0") double repairCharge,
                             RedirectAttributes redirect) {

        if (damaged && damageNote.isBlank()) {
            redirect.addFlashAttribute("error", "Please write what is damaged.");
            return "redirect:/admin/rentals/" + id + "/return";
        }
        if (repairCharge < 0) {
            repairCharge = 0;
        }

        String error = rentalService.returnRental(id, returnDate, damaged, damageNote.trim(), repairCharge);

        if (error != null) {
            redirect.addFlashAttribute("error", error);
            return "redirect:/admin/rentals/" + id + "/return";
        }

        // short bill shown on top of the rentals page
        // (the rupee sign is written with its unicode number 20B9)
        Rental rental = rentalService.getRental(id);
        String bill = "Item returned. Rent \u20B9" + Math.round(rental.getRentAmount())
                + " + late fee \u20B9" + Math.round(rental.getLateFee())
                + " (" + rental.getLateDays() + " late days)"
                + " + damage \u20B9" + Math.round(rental.getDamageCharge())
                + " = \u20B9" + Math.round(rental.getTotal());
        redirect.addFlashAttribute("message", bill);
        return "redirect:/admin/rentals";
    }

    // ---------- damage reports ----------

    @GetMapping("/admin/damage")
    public String damageReports(Model model) {
        model.addAttribute("reports", damageService.getAllReports());
        return "admin/damage";
    }

    @PostMapping("/admin/damage/{id}/repaired")
    public String repaired(@PathVariable String id, RedirectAttributes redirect) {
        damageService.markRepaired(id);
        redirect.addFlashAttribute("message", "Item repaired and available again.");
        return "redirect:/admin/damage";
    }
}
