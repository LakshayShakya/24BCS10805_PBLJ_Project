package com.renttrack.service;

import com.renttrack.model.Equipment;
import com.renttrack.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Everything related to the equipment list and its availability.
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepo;

    public EquipmentService(EquipmentRepository equipmentRepo) {
        this.equipmentRepo = equipmentRepo;
    }

    // Items that match the chosen category and the search word.
    public List<Equipment> findItems(String category, String search) {
        String word = search.trim().toLowerCase();
        List<Equipment> result = new ArrayList<>();

        for (Equipment item : equipmentRepo.findAll()) {
            boolean categoryOk = category.equals("All") || item.getCategory().equals(category);
            boolean nameOk = item.getName().toLowerCase().contains(word);
            if (categoryOk && nameOk) {
                result.add(item);
            }
        }
        return result;
    }

    public Equipment getItem(String id) {
        return equipmentRepo.findById(id).orElse(null);
    }

    // Admin adds a new item. Returns an error message, or null if it worked.
    public String addItem(String name, String category, String description, double dailyRate) {
        String error = checkDetails(name, dailyRate);
        if (error != null) {
            return error;
        }
        equipmentRepo.save(new Equipment(name.trim(), category, description.trim(), dailyRate));
        return null;
    }

    // Admin changes the details of an existing item.
    public String updateItem(String id, String name, String category, String description, double dailyRate) {
        Equipment item = getItem(id);
        if (item == null) {
            return "Item not found.";
        }
        String error = checkDetails(name, dailyRate);
        if (error != null) {
            return error;
        }
        item.setName(name.trim());
        item.setCategory(category);
        item.setDescription(description.trim());
        item.setDailyRate(dailyRate);
        equipmentRepo.save(item);
        return null;
    }

    private String checkDetails(String name, double dailyRate) {
        if (name.isBlank()) {
            return "Please enter the item name.";
        }
        if (dailyRate <= 0) {
            return "Rent per day must be greater than zero.";
        }
        return null;
    }

    public long countAll() {
        return equipmentRepo.count();
    }

    public long countByStatus(String status) {
        return equipmentRepo.countByStatus(status);
    }
}
