package br.com.api_mediway.patient.dto.response;

import java.util.UUID;

public record PatientSummaryDTO(
        UUID userId,
        String fullName,
        String conditionPatient
) {
}
