package br.com.api_mediway.dto.request.exam;

public record UpdateExamDTO(
        String typeExam,
        String local,
        String requeriments
) {
}
