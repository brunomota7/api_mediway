package br.com.api_mediway.dto.request.consultation;

import java.util.UUID;

public record ConsultationRequestDTO(
        UUID doctorId,
        String description,
        String localConsultation,
        String requeriments
) {
}
