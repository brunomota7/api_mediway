package br.com.api_mediway.dto.response.caregiver;

import br.com.api_mediway.enums.DoctorAndCaregiverAvailability;

import java.util.List;
import java.util.UUID;

public record CaregiverResponseDTO(
        Long caregiverInfoId,
        UUID userId,
        String name,
        String email,
        String specialty,
        String experience,
        DoctorAndCaregiverAvailability availability,
        List<UUID> patientUserIds
) {
}

