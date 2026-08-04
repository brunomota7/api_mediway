package br.com.api_mediway.exam.dto.request;

import java.time.LocalDate;

public record ExamCancellationRequestDTO(
        LocalDate cancellationDate
) {
}
