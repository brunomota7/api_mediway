package br.com.api_mediway.dto.response.doctor;

import br.com.api_mediway.enums.DoctorAndCaregiverAvailability;

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

