package br.com.api_mediway.dto.response.patient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PatientResponseInfosDTO(
        UUID patientI,
        PersonalInfoDTO personalInfo,
        ContactInfoDTO contactInfo,
        MedicalInfoDTO medicalInfo
) {
    public record PersonalInfoDTO(
            String name,
            LocalDate dateOfBirth,
            Integer age,
            String gender,
            List<String> roles
    ) {}
    public record ContactInfoDTO(
            String email,
            String number
    ) {}
    public record MedicalInfoDTO(
            String conditionPatient,
            String statusPatient
    ) {}
}
