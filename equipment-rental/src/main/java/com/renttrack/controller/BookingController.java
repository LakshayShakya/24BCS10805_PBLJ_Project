package com.renttrack.controller;

import com.renttrack.model.Equipment;
import com.renttrack.model.User;
import com.renttrack.service.EquipmentService;
import com.renttrack.service.RentalService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

// Pages of a logged in user: book an item and see his own rentals.
@Controller
public class BookingController {

    private final EquipmentService equipmentService;
    private final RentalService rentalService;

    public BookingController(EquipmentService equipmentService, RentalService rentalService) {
        this.equipmentService = equipmentService;
        this.rentalService = rentalService;
    }

    @GetMapping("/book/{itemId}")
    public String bookingForm(@PathVariable String itemId, Model model, RedirectAttributes redirect) {
        Equipment item = equipmentService.getItem(itemId);

        if (item == null || !item.getStatus().equals(Equipment.AVAILABLE)) {
            redirect.addFlashAttribute("error", "This item is not available right now.");
            return "redirect:/";
        }
        model.addAttribute("item", item);
        model.addAttribute("today", LocalDate.now());
        return "book";
    }

    @PostMapping("/book/{itemId}")
    public String saveBooking(@PathVariable String itemId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                              HttpSession session,
                              RedirectAttributes redirect) {

        User user = (User) session.getAttribute("loggedUser");
        String error = rentalService.bookRental(itemId, user, startDate, dueDate);

        if (error != null) {
            redirect.addFlashAttribute("error", error);
            return "redirect:/book/" + itemId;
        }
        redirect.addFlashAttribute("message", "Booking confirmed. Collect the item from the shop on " + startDate + ".");
        return "redirect:/my-rentals";
    }

    @GetMapping("/my-rentals")
    public String myRentals(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("rentals", rentalService.getRentalsOfUser(user));
        return "myRentals";
    }
}
