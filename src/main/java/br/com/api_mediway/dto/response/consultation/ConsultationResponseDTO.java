package br.com.api_mediway.dto.response.consultation;

import br.com.api_mediway.enums.ConsultationAndExmStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultationResponseDTO(
        Long consultationId,
        ConsultationAndExmStatus status,
        PatientDTO patient,
        DoctorDto doctor,
        ConsultationDetailsDTO details
) {
    public record PatientDTO(
            String name,
            String email,
            String number
    ) {}
    public record DoctorDto(
            String name,
            String specialty
    ) {}
    public record ConsultationDetailsDTO(
            LocalDate  consultationDate,
            LocalTime consultationTime,
            String localConsultation,
            String description,
            String requeriments
    ) {}
}
