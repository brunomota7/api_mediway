package br.com.api_mediway.medication.dto.request;

import br.com.api_mediway.medication.enums.MedicationStatus;
import br.com.api_mediway.medication.enums.MedicationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalTime;
import java.util.List;

public record MedicationRequestDTO(
        @NotBlank String nome,
        @NotNull MedicationType tipo,
        String nomeReferencia,
        String descricao,
        String concentracao,
        String quantidade,

        @NotEmpty List<String> dias,
        @NotNull LocalTime hora,
        String gaveta,
        @NotNull @PositiveOrZero Integer estoque,

        @NotNull MedicationStatus status,
        @NotNull Long medicineBoxId
) {
}
