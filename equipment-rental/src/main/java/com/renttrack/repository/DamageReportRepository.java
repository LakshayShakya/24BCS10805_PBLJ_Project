package com.renttrack.repository;

import com.renttrack.model.DamageReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DamageReportRepository extends MongoRepository<DamageReport, String> {

    List<DamageReport> findAllByOrderByIdDesc();
}
