package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.exam.ExamRequestDTO;
import br.com.api_mediway.entites.Exam;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.enums.ConsultationAndExmStatus;

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
