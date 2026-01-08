package br.com.api_mediway.dto.response.patient;

import java.util.UUID;

public record PatientSummaryDTO(
        UUID userId,
        String fullName,
        String conditionPatient
) {
}
