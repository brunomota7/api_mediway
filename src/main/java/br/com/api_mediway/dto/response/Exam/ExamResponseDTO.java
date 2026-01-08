package br.com.api_mediway.dto.response.Exam;

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
            String typeExam,
            String requeriments,
            LocalDate cancellationDate,
            LocalDate date,
            LocalTime startTime,
            String local
    ) {}
}
