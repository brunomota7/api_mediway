package br.com.api_mediway.caregiver.dto.request;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CaregiverRequestDTO(
        @NotBlank String specialty,
        @Size(max = 500) String experience,
        @NotNull DoctorAndCaregiverAvailability availability
) {
}