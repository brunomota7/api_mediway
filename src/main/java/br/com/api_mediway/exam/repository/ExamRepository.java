package br.com.api_mediway.exam.repository;

import br.com.api_mediway.exam.entity.Exam;
import br.com.api_mediway.common.enums.ConsultationAndExmStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByPatientInfosUserUserId(UUID patientId);
    List<Exam> findByExamDate(LocalDate date);
    List<Exam> findByStatus(ConsultationAndExmStatus status);
}
