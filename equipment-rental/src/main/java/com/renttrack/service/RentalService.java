package com.renttrack.service;

import com.renttrack.model.DamageReport;
import com.renttrack.model.Equipment;
import com.renttrack.model.Rental;
import com.renttrack.model.User;
import com.renttrack.repository.DamageReportRepository;
import com.renttrack.repository.EquipmentRepository;
import com.renttrack.repository.RentalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Rules for booking and returning, including the late fee.
// Controllers call these methods and never touch the database directly.
@Service
public class RentalService {

    // Every late day costs 1.5 times the normal daily rent.
    public static final double LATE_FEE_MULTIPLIER = 1.5;

    private final EquipmentRepository equipmentRepo;
    private final RentalRepository rentalRepo;
    private final DamageReportRepository damageRepo;

    public RentalService(EquipmentRepository equipmentRepo,
                         RentalRepository rentalRepo,
                         DamageReportRepository damageRepo) {
        this.equipmentRepo = equipmentRepo;
        this.rentalRepo = rentalRepo;
        this.damageRepo = damageRepo;
    }

    // Saves a new booking. Returns an error message, or null if it worked.
    public String bookRental(String equipmentId, User customer,
                             LocalDate startDate, LocalDate dueDate) {

        Equipment item = equipmentRepo.findById(equipmentId).orElse(null);

        if (item == null) {
            return "Please select an item.";
        }
        if (!item.getStatus().equals(Equipment.AVAILABLE)) {
            return item.getName() + " is not available right now.";
        }
        if (startDate.isBefore(LocalDate.now())) {
            return "Start date cannot be in the past.";
        }
        if (dueDate.isBefore(startDate)) {
            return "Due date cannot be before the start date.";
        }

        // same-day return is still charged as one day
        long days = ChronoUnit.DAYS.between(startDate, dueDate);
        if (days == 0) {
            days = 1;
        }

        Rental rental = new Rental();
        rental.setEquipment(item);
        rental.setCustomerId(customer.getId());
        rental.setCustomerName(customer.getFullName());
        rental.setPhone(customer.getPhone());
        rental.setStartDate(startDate);
        rental.setDueDate(dueDate);
        rental.setDailyRate(item.getDailyRate());
        rental.setRentAmount(days * item.getDailyRate());
        rentalRepo.save(rental);

        // availability tracking: item is now with the customer
        item.setStatus(Equipment.RENTED);
        equipmentRepo.save(item);

        return null;
    }

    // late fee = late days x daily rate x 1.5
    // The rate saved in the booking is used, so the bill does not change
    // if the admin edits the price later.
    public double calculateLateFee(Rental rental) {
        return rental.getLateDays() * rental.getDailyRate() * LATE_FEE_MULTIPLIER;
    }

    // Closes a rental. Returns an error message, or null if it worked.
    public String returnRental(String rentalId, LocalDate returnDate, boolean damaged,
                               String damageNote, double repairCharge) {

        Rental rental = rentalRepo.findById(rentalId).orElse(null);

        if (rental == null || rental.isReturned()) {
            return "This rental is already closed.";
        }
        if (returnDate.isBefore(rental.getStartDate())) {
            return "Return date cannot be before the start date.";
        }
        if (returnDate.isAfter(LocalDate.now())) {
            return "Return date cannot be in the future.";
        }

        Equipment item = rental.getEquipment();

        rental.setReturnDate(returnDate);
        rental.setLateFee(calculateLateFee(rental));
        rental.setReturned(true);

        if (damaged) {
            rental.setDamageCharge(repairCharge);

            DamageReport report = new DamageReport();
            report.setEquipment(item);
            report.setCustomerName(rental.getCustomerName());
            report.setDescription(damageNote);
            report.setRepairCharge(repairCharge);
            report.setReportedOn(returnDate);
            damageRepo.save(report);

            // a broken item cannot be booked until it is fixed
            item.setStatus(Equipment.REPAIR);
        } else {
            item.setStatus(Equipment.AVAILABLE);
        }

        rentalRepo.save(rental);
        equipmentRepo.save(item);
        return null;
    }

    // ---------- reading data for the pages ----------

    public List<Rental> getAllRentals() {
        return rentalRepo.findAllByOrderByIdDesc(); // newest first
    }

    // bookings made by one user (for the "My Rentals" page)
    public List<Rental> getRentalsOfUser(User customer) {
        return rentalRepo.findByCustomerIdOrderByIdDesc(customer.getId());
    }

    public Rental getRental(String rentalId) {
        return rentalRepo.findById(rentalId).orElse(null);
    }

    // running rentals whose due date has already passed
    public List<Rental> getOverdueRentals() {
        List<Rental> overdue = new ArrayList<>();
        for (Rental rental : rentalRepo.findByReturnedFalse()) {
            if (rental.getLateDays() > 0) {
                overdue.add(rental);
            }
        }
        return overdue;
    }

    // money collected from the rentals that are closed
    public double getTotalCollected() {
        double collected = 0;
        for (Rental rental : rentalRepo.findAll()) {
            if (rental.isReturned()) {
                collected = collected + rental.getTotal();
            }
        }
        return collected;
    }
}
