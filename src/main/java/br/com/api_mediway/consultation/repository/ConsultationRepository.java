package br.com.api_mediway.consultation.repository;

import br.com.api_mediway.consultation.entity.Consultation;
import br.com.api_mediway.common.enums.ConsultationAndExmStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientInfosUserUserId(UUID patientId);
    List<Consultation> findByConsultationDate(LocalDate date);
    List<Consultation> findByDoctorInfosUserUserId(UUID doctorId);
    List<Consultation> findByStatus(ConsultationAndExmStatus status);
}
