package com.renttrack.repository;

import com.renttrack.model.Rental;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RentalRepository extends MongoRepository<Rental, String> {

    // rentals that are still running
    List<Rental> findByReturnedFalse();

    // bookings of one user, newest first
    List<Rental> findByCustomerIdOrderByIdDesc(String customerId);

    // newest booking first
    List<Rental> findAllByOrderByIdDesc();
}
