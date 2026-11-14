package com.renttrack.repository;

import com.renttrack.model.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// Spring builds the MongoDB query for these methods by reading their names.
public interface EquipmentRepository extends MongoRepository<Equipment, String> {

    List<Equipment> findByStatus(String status);

    long countByStatus(String status);
}
