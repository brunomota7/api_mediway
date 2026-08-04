package br.com.api_mediway.consultation.factory;

import br.com.api_mediway.consultation.dto.request.ConsultationRequestDTO;
import br.com.api_mediway.consultation.entity.Consultation;
import br.com.api_mediway.doctor.entity.DoctorInfos;
import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.common.enums.ConsultationAndExmStatus;

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
