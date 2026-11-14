package com.renttrack.service;

import com.renttrack.model.DamageReport;
import com.renttrack.model.Equipment;
import com.renttrack.repository.DamageReportRepository;
import com.renttrack.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Damage reports and what happens after the repair.
@Service
public class DamageService {

    private final DamageReportRepository damageRepo;
    private final EquipmentRepository equipmentRepo;

    public DamageService(DamageReportRepository damageRepo, EquipmentRepository equipmentRepo) {
        this.damageRepo = damageRepo;
        this.equipmentRepo = equipmentRepo;
    }

    public List<DamageReport> getAllReports() {
        return damageRepo.findAllByOrderByIdDesc(); // newest first
    }

    // Called when the repair work is over: the item can be booked again.
    public void markRepaired(String reportId) {
        DamageReport report = damageRepo.findById(reportId).orElse(null);
        if (report == null) {
            return;
        }
        report.setRepaired(true);
        damageRepo.save(report);

        Equipment item = report.getEquipment();
        if (item.getStatus().equals(Equipment.REPAIR)) {
            item.setStatus(Equipment.AVAILABLE);
            equipmentRepo.save(item);
        }
    }
}
