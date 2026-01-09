package br.com.api_mediway.dto.response.consultation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultationSummaryDTO(
        Long consultationId,
        LocalDate  consultationDate,
        LocalTime consultationTime,
        String localConsultation,
        String status
) {
}
