package br.com.api_mediway.doctor.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;

public record UpdateDoctorInfosDTO(
        String name,
        String email,
        String number,
        String specialty,
        DoctorAndCaregiverAvailability availability,
        String imageUrl
) {
}
