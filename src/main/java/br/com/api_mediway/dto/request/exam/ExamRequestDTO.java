package br.com.api_mediway.dto.request.exam;

import java.time.LocalDate;
import java.time.LocalTime;

public record ExamRequestDTO(
        LocalDate examDate,
        LocalTime examTime,
        String typeExam,
        String local,
        String requeriments
) {
}
