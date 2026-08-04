package br.com.api_mediway.exam.mapper;


import br.com.api_mediway.exam.dto.response.ExamResponseDTO;
import br.com.api_mediway.exam.entity.Exam;

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
