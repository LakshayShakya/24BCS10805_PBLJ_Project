package com.renttrack.repository;

import com.renttrack.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    // gives null when nobody has this email
    User findByEmail(String email);

    List<User> findByRole(String role);
}
