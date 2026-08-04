package br.com.api_mediway.exam.dto.request;

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
