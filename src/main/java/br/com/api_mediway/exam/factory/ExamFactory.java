package br.com.api_mediway.exam.factory;

import br.com.api_mediway.exam.dto.request.ExamRequestDTO;
import br.com.api_mediway.exam.entity.Exam;
import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.common.enums.ConsultationAndExmStatus;

public class ExamFactory {

    public static Exam registerExam(
            PatientInfos patient,
            ExamRequestDTO dto
    ) {
        Exam exam = new Exam();
        exam.setPatientInfos(patient);
        exam.setTypeExam(dto.typeExam());
        exam.setLocal(dto.local());
        exam.setRequeriments(dto.requeriments());
        exam.setStatus(ConsultationAndExmStatus.MARCADO);
        return exam;
    }

}
