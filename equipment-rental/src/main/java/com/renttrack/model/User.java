package com.renttrack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

// A person who can log in. The role decides what he can open:
// ADMIN manages the shop, USER browses and books.
@Document(collection = "users")
public class User implements Serializable {

    public static final String ADMIN = "ADMIN";
    public static final String CUSTOMER = "USER";

    @Id
    private String id;

    private String fullName;
    private String email;      // used as the login name
    private String phone;
    private String password;   // saved in hashed form, never as plain text
    private String role;

    public boolean isAdmin() {
        return ADMIN.equals(role);
    }

    public String getId() { return id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
