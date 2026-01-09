package br.com.api_mediway.dto.request.consultation;

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
