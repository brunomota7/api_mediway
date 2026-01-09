package br.com.api_mediway.dto.request.consultation;

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
