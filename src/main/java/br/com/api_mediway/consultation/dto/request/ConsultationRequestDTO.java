package br.com.api_mediway.consultation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ConsultationRequestDTO(
        UUID doctorId,
        LocalDate  consultationDate,
        LocalTime consultationTime,
        String description,
        String localConsultation,
        String requeriments
) {
}
