package com.renttrack.config;

import com.renttrack.model.Equipment;
import com.renttrack.model.Rental;
import com.renttrack.model.User;
import com.renttrack.repository.EquipmentRepository;
import com.renttrack.repository.RentalRepository;
import com.renttrack.repository.UserRepository;
import com.renttrack.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// Runs once when the server starts. Creates the admin account, one demo
// user, a few items and one late rental, so the website is not empty
// during the demo.
@Component
public class SampleData implements CommandLineRunner {

    private final EquipmentRepository equipmentRepo;
    private final RentalRepository rentalRepo;
    private final UserRepository userRepo;
    private final UserService userService;

    public SampleData(EquipmentRepository equipmentRepo, RentalRepository rentalRepo,
                      UserRepository userRepo, UserService userService) {
        this.equipmentRepo = equipmentRepo;
        this.rentalRepo = rentalRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (!userService.emailExists("admin@renttrack.com")) {
            userService.createUser("Shop Admin", "admin@renttrack.com", "9000000001", "admin123", User.ADMIN);
        }
        if (!userService.emailExists("user@renttrack.com")) {
            userService.createUser("Demo User", "user@renttrack.com", "9000000002", "user123", User.CUSTOMER);
        }

        if (equipmentRepo.count() > 0) {
            return; // items are already there
        }
        equipmentRepo.save(new Equipment("Canon EOS 200D", "Camera", "24 MP DSLR with 18-55mm kit lens and bag", 800));
        equipmentRepo.save(new Equipment("Sony Alpha A6400", "Camera", "Mirrorless camera, good for video shoots", 1200));
        equipmentRepo.save(new Equipment("Dell Latitude 5420", "Laptop", "i5, 16 GB RAM, 512 GB SSD, charger included", 600));
        equipmentRepo.save(new Equipment("MacBook Air M1", "Laptop", "13 inch, 8 GB RAM, long battery life", 1000));
        equipmentRepo.save(new Equipment("Epson EB-X06 Projector", "Projector", "3600 lumens, HDMI cable and remote", 700));
        equipmentRepo.save(new Equipment("BenQ MW560 Projector", "Projector", "Bright WXGA projector for halls", 900));
        equipmentRepo.save(new Equipment("Aluminium Ladder 10ft", "Tool", "Foldable ladder, holds up to 120 kg", 150));

        Equipment drill = new Equipment("Bosch Drill Machine", "Tool", "13 mm impact drill with bit set", 250);
        equipmentRepo.save(drill);
        addOverdueRental(drill);
    }

    // One old booking of the demo user that is already 2 days late.
    // It is here so the late fee can be shown without waiting for real days to pass.
    private void addOverdueRental(Equipment item) {
        User demoUser = userRepo.findByEmail("user@renttrack.com");

        Rental rental = new Rental();
        rental.setEquipment(item);
        rental.setCustomerId(demoUser.getId());
        rental.setCustomerName(demoUser.getFullName());
        rental.setPhone(demoUser.getPhone());
        rental.setStartDate(LocalDate.now().minusDays(5));
        rental.setDueDate(LocalDate.now().minusDays(2));
        rental.setDailyRate(item.getDailyRate());
        rental.setRentAmount(3 * item.getDailyRate());
        rentalRepo.save(rental);

        item.setStatus(Equipment.RENTED);
        equipmentRepo.save(item);
    }
}
