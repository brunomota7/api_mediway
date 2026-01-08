package br.com.api_mediway.dto.request.consultation;

public record UpdateConsultationDTO(
        String description,
        String localConsultation,
        String requeriments
) {
}
