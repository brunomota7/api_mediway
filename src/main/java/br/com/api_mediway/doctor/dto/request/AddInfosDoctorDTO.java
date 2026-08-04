package br.com.api_mediway.doctor.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;

public record AddInfosDoctorDTO(
        String crm,
        String specialty,
        DoctorAndCaregiverAvailability availability
) {
}
