package br.com.api_mediway.exam.dto.request;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateExamDTO(
        @FutureOrPresent LocalDate examDate,
        LocalTime examTime,
        String typeExam,
        String local,
        String requeriments
) {
}