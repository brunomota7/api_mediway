package br.com.api_mediway.consultation.mapper;


import br.com.api_mediway.consultation.dto.response.ConsultationResponseDTO;
import br.com.api_mediway.consultation.entity.Consultation;

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
