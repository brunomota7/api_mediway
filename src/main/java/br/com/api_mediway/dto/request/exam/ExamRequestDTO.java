package br.com.api_mediway.dto.request.exam;

public record ExamRequestDTO(
        String typeExam,
        String local,
        String requeriments
) {
}
