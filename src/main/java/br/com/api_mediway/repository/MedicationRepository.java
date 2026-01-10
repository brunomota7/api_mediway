package br.com.api_mediway.repository;

import br.com.api_mediway.entites.Medication;
import br.com.api_mediway.enums.MedicationStatus;
import br.com.api_mediway.enums.MedicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPatientInfosUserUserId(UUID userId);
    List<Medication> findByMedicineBoxMedicineBoxId(Long medicineBoxId);
    List<Medication> findByMedicineBoxMedicineBoxIdAndGaveta(Long medicineBoxId, String gaveta);
}

