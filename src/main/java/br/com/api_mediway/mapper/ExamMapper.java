package br.com.api_mediway.mapper;


import br.com.api_mediway.dto.response.Exam.ExamResponseDTO;
import br.com.api_mediway.entites.Exam;

public class ExamMapper {

    public static ExamResponseDTO toResponse(Exam exam) {
        var patientUser = exam.getPatientInfos().getUser();

        return new ExamResponseDTO(
                exam.getExamId(),
                exam.getRequestDate(),
                new ExamResponseDTO.PatientDTO(
                        patientUser.getName(),
                        patientUser.getEmail()
                ),
                new ExamResponseDTO.ExamDetailsDTO(
                        exam.getExamDate(),
                        exam.getExamTime(),
                        exam.getTypeExam(),
                        exam.getRequeriments(),
                        exam.getStatus(),
                        exam.getLocal()
                )
        );
    }

}
