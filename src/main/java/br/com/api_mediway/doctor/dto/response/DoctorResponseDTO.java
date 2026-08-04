package br.com.api_mediway.doctor.dto.response;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;

import java.util.List;
import java.util.UUID;

public record DoctorResponseDTO(
        UUID doctorId,
        PersonalInfoDTO personalInfo,
        ContactInfoDTO contactInfo,
        ProfessionalInfoDTO professionalInfo
) {
    public record PersonalInfoDTO(
            String name,
            List<String> roles
    ) {}
    public record ContactInfoDTO(
            String email,
            String number
    ) {}
    public record ProfessionalInfoDTO(
            String crm,
            String specialty,
            DoctorAndCaregiverAvailability availability
    ) {}
}

