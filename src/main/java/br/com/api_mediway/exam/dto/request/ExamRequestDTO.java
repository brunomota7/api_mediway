package br.com.api_mediway.exam.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ExamRequestDTO(
        @NotNull @FutureOrPresent LocalDate examDate,
        @NotNull LocalTime examTime,
        @NotBlank String typeExam,
        @NotBlank String local,
        String requeriments
) {
}