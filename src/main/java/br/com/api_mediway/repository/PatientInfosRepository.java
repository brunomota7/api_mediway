package br.com.api_mediway.repository;

import br.com.api_mediway.entites.PatientInfos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientInfosRepository extends JpaRepository<PatientInfos, Long> {
    Optional<PatientInfos> findByUserUserId(UUID userId);
    Optional<PatientInfos> findByUserEmail(String email);
//    List<PatientInfos> findByDoctorsUserUserId(UUID doctorId);
}
