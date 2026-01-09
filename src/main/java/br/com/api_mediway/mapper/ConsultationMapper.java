package br.com.api_mediway.mapper;


import br.com.api_mediway.dto.response.consultation.ConsultationResponseDTO;
import br.com.api_mediway.entites.Consultation;

public class ConsultationMapper {

    public static ConsultationResponseDTO toResponse(Consultation consultation) {
        var patient = consultation.getPatientInfos().getUser();
        var doctor = consultation.getDoctorInfos().getUser();

        return new ConsultationResponseDTO(
                consultation.getConsultationId(),
                consultation.getStatus(),
                new ConsultationResponseDTO.PatientDTO(
                        patient.getName(),
                        patient.getEmail(),
                        patient.getNumber()
                ),
                new ConsultationResponseDTO.DoctorDto(
                        doctor.getName(),
                        consultation.getDoctorInfos().getSpecialty()
                ),
                new ConsultationResponseDTO.ConsultationDetailsDTO(
                        consultation.getConsultationDate(),
                        consultation.getConsultationTime(),
                        consultation.getLocalConsultation(),
                        consultation.getDescription(),
                        consultation.getRequeriments()
                )
        );
    }

}
