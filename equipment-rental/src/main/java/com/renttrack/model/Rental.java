package com.renttrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// One booking: which item, which user took it, for which dates and the bill.
@Document(collection = "rentals")
public class Rental {

    @Id
    private String id;   // MongoDB creates this id by itself

    @DBRef   // stores a link to the equipment document
    private Equipment equipment;

    // details of the user who booked (copied at booking time)
    private String customerId;
    private String customerName;
    private String phone;

    private LocalDate startDate;
    private LocalDate dueDate;     // date promised by the customer
    private LocalDate returnDate;  // stays empty until the item comes back

    private double dailyRate;      // rate on the day of booking
    private double rentAmount;     // days x daily rate
    private double lateFee;
    private double damageCharge;
    private boolean returned;

    // Days after the due date. For a running rental we count till today.
    public long getLateDays() {
        LocalDate lastDay = (returnDate != null) ? returnDate : LocalDate.now();
        long days = ChronoUnit.DAYS.between(dueDate, lastDay);
        return Math.max(days, 0);
    }

    public double getTotal() {
        return rentAmount + lateFee + damageCharge;
    }

    public String getId() { return id; }

    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public double getRentAmount() { return rentAmount; }
    public void setRentAmount(double rentAmount) { this.rentAmount = rentAmount; }

    public double getLateFee() { return lateFee; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }

    public double getDamageCharge() { return damageCharge; }
    public void setDamageCharge(double damageCharge) { this.damageCharge = damageCharge; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
