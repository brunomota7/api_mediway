package br.com.api_mediway.dto.request.doctor;

import br.com.api_mediway.enums.DoctorAndCaregiverAvailability;

public record AddInfosDoctorDTO(
        String crm,
        String specialty,
        DoctorAndCaregiverAvailability availability
) {
}
