package br.com.api_mediway.doctor.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import jakarta.validation.constraints.Email;

public record UpdateDoctorInfosDTO(
        String name,
        @Email String email,
        String number,
        String specialty,
        DoctorAndCaregiverAvailability availability,
        String imageUrl
) {
}