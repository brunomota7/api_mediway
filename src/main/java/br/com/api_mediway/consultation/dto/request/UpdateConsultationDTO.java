package br.com.api_mediway.consultation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateConsultationDTO(
        LocalDate consultationDate,
        LocalTime consultationTime,
        String description,
        String localConsultation,
        String requeriments
) {
}
