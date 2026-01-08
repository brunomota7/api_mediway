package br.com.api_mediway.dto.request.exam;

import java.time.LocalDate;

public record ExamCancellationRequestDTO(
        LocalDate cancellationDate
) {
}
