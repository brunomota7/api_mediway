package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.consultation.ConsultationRequestDTO;
import br.com.api_mediway.entites.Consultation;
import br.com.api_mediway.entites.DoctorInfos;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.enums.ConsultationAndExmStatus;

public class ConsultationFactory {

    public static Consultation registerConsultation(
            PatientInfos patient,
            DoctorInfos doctor,
            ConsultationRequestDTO dto
    ) {
        Consultation consultation = new Consultation();
        consultation.setPatientInfos(patient);
        consultation.setDescription(dto.description());
        consultation.setLocalConsultation(dto.localConsultation());
        consultation.setRequeriments(ConsultationAndExmStatus.MARCADO.getLabel());
        consultation.setDoctorInfos(doctor);
        return consultation;
    }

}
