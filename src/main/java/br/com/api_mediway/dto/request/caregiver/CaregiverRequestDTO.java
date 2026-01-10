package br.com.api_mediway.dto.request.caregiver;

import br.com.api_mediway.enums.DoctorAndCaregiverAvailability;

public record CaregiverRequestDTO(
        String specialty,
        String experience,
        DoctorAndCaregiverAvailability availability
) {
}

