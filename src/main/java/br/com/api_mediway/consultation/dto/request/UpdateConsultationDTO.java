package br.com.api_mediway.consultation.dto.request;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateConsultationDTO(
        @FutureOrPresent LocalDate consultationDate,
        LocalTime consultationTime,
        String description,
        String localConsultation,
        String requeriments
) {
}