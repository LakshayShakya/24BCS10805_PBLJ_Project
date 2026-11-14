package com.renttrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// One item that the shop gives on rent. Saved in the "equipment" collection.
@Document(collection = "equipment")
public class Equipment {

    // the three states an item can be in
    public static final String AVAILABLE = "AVAILABLE";
    public static final String RENTED = "RENTED";
    public static final String REPAIR = "REPAIR";

    @Id
    private String id;   // MongoDB creates this id by itself

    private String name;
    private String category;   // Camera, Laptop, Projector or Tool
    private String description;
    private double dailyRate;  // rent for one day
    private String status;

    public Equipment() {
    }

    public Equipment(String name, String category, String description, double dailyRate) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.dailyRate = dailyRate;
        this.status = AVAILABLE; // a new item is always free
    }

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
