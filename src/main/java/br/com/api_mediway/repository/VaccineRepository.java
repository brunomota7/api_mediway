package br.com.api_mediway.repository;

import br.com.api_mediway.entites.Vaccine;
import br.com.api_mediway.enums.VaccineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findByPatientInfosUserUserId(UUID patientId);
    List<Vaccine> findByStatus(VaccineStatus status);
    List<Vaccine> findByProximaDose(LocalDate date);
}
