package com.renttrack.service;

import com.renttrack.model.User;
import com.renttrack.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// Sign up and login checks.
@Service
public class UserService {

    private final UserRepository userRepo;

    // BCrypt turns a password into a hash that cannot be turned back
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Sign up for a normal user. Returns an error message, or null if it worked.
    public String register(String fullName, String email, String phone, String password) {
        email = email.trim().toLowerCase();

        if (fullName.isBlank() || email.isBlank()) {
            return "Name and email are required.";
        }
        if (!email.contains("@") || email.contains(" ")) {
            return "Please enter a valid email address.";
        }
        if (!phone.matches("[0-9]{10}")) {
            return "Phone number must have 10 digits.";
        }
        if (password.length() < 6) {
            return "Password must have at least 6 characters.";
        }
        if (userRepo.findByEmail(email) != null) {
            return "An account with this email already exists.";
        }

        createUser(fullName, email, phone, password, User.CUSTOMER);
        return null;
    }

    public void createUser(String fullName, String email, String phone, String password, String role) {
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        userRepo.save(user);
    }

    // Returns the user when email and password are correct, otherwise null.
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email.trim().toLowerCase());

        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email) != null;
    }

    public List<User> getCustomers() {
        return userRepo.findByRole(User.CUSTOMER);
    }
}
