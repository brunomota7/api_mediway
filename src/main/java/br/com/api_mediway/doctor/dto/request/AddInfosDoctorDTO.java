package br.com.api_mediway.doctor.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddInfosDoctorDTO(
        @NotBlank String crm,
        @NotBlank String specialty,
        @NotNull DoctorAndCaregiverAvailability availability
) {
}