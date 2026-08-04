package br.com.api_mediway.medicinebox.repository;

import br.com.api_mediway.medicinebox.entity.MedicineBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MedicineBoxRepository extends JpaRepository<MedicineBox, Long> {
    Optional<MedicineBox> findByPatientInfosUserUserId(UUID userId);
    boolean existsByPatientInfosUserUserId(UUID userId);
}
