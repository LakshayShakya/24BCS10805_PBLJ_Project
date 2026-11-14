package com.renttrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;

// Written when an item comes back broken.
@Document(collection = "damage_reports")
public class DamageReport {

    @Id
    private String id;   // MongoDB creates this id by itself

    @DBRef   // stores a link to the equipment document
    private Equipment equipment;

    private String customerName;   // who returned it damaged
    private String description;
    private double repairCharge;
    private LocalDate reportedOn;
    private boolean repaired;

    public String getId() { return id; }

    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getRepairCharge() { return repairCharge; }
    public void setRepairCharge(double repairCharge) { this.repairCharge = repairCharge; }

    public LocalDate getReportedOn() { return reportedOn; }
    public void setReportedOn(LocalDate reportedOn) { this.reportedOn = reportedOn; }

    public boolean isRepaired() { return repaired; }
    public void setRepaired(boolean repaired) { this.repaired = repaired; }
}
