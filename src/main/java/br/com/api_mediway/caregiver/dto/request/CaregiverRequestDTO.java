package br.com.api_mediway.caregiver.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;

public record CaregiverRequestDTO(
        String specialty,
        String experience,
        DoctorAndCaregiverAvailability availability
) {
}

