package br.com.api_mediway.dto.response.Exam;

import br.com.api_mediway.enums.ConsultationAndExmStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ExamResponseDTO(
        Long examId,
        LocalDateTime requestDate,
        PatientDTO patient,
        ExamDetailsDTO exam
) {
    public record PatientDTO(
            String name,
            String email
    ) {}
    public record ExamDetailsDTO(
            LocalDate examDate,
            LocalTime examTime,
            String typeExam,
            String requeriments,
            ConsultationAndExmStatus status,
            String local
    ) {}
}
