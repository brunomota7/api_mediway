package br.com.api_mediway.dto.request.doctor;

import br.com.api_mediway.enums.DoctorAndCaregiverAvailability;

public record UpdateDoctorInfosDTO(
        String name,
        String email,
        String number,
        String specialty,
        DoctorAndCaregiverAvailability availability,
        String imageUrl
) {
}
