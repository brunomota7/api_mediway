package br.com.api_mediway.consultation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ConsultationRequestDTO(
        @NotNull UUID doctorId,
        @NotNull @FutureOrPresent LocalDate consultationDate,
        @NotNull LocalTime consultationTime,
        @NotBlank String description,
        @NotBlank String localConsultation,
        String requeriments
) {
}