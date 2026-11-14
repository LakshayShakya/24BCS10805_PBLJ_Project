package com.renttrack.controller;

import com.renttrack.model.Equipment;
import com.renttrack.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Admin: see all equipment, add a new item, update the details.
@Controller
public class AdminEquipmentController {

    private final EquipmentService equipmentService;

    public AdminEquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/admin/equipment")
    public String list(Model model) {
        model.addAttribute("items", equipmentService.findItems("All", ""));
        return "admin/equipment";
    }

    @PostMapping("/admin/equipment/add")
    public String add(@RequestParam String name,
                      @RequestParam String category,
                      @RequestParam(defaultValue = "") String description,
                      @RequestParam double dailyRate,
                      RedirectAttributes redirect) {

        String error = equipmentService.addItem(name, category, description, dailyRate);

        if (error != null) {
            redirect.addFlashAttribute("error", error);
        } else {
            redirect.addFlashAttribute("message", name.trim() + " added to the list.");
        }
        return "redirect:/admin/equipment";
    }

    @GetMapping("/admin/equipment/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        Equipment item = equipmentService.getItem(id);
        if (item == null) {
            return "redirect:/admin/equipment";
        }
        model.addAttribute("item", item);
        return "admin/equipmentEdit";
    }

    @PostMapping("/admin/equipment/{id}/edit")
    public String update(@PathVariable String id,
                         @RequestParam String name,
                         @RequestParam String category,
                         @RequestParam(defaultValue = "") String description,
                         @RequestParam double dailyRate,
                         RedirectAttributes redirect) {

        String error = equipmentService.updateItem(id, name, category, description, dailyRate);

        if (error != null) {
            redirect.addFlashAttribute("error", error);
            return "redirect:/admin/equipment/" + id + "/edit";
        }
        redirect.addFlashAttribute("message", "Details updated.");
        return "redirect:/admin/equipment";
    }
}
