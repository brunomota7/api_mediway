package br.com.api_mediway.medication.dto.request;

import br.com.api_mediway.medication.enums.MedicationStatus;
import br.com.api_mediway.medication.enums.MedicationType;

import java.time.LocalTime;
import java.util.List;

public record MedicationRequestDTO(
        String nome,
        MedicationType tipo,
        String nomeReferencia,
        String descricao,
        String concentracao,
        String quantidade,

        List<String> dias,
        LocalTime hora,
        String gaveta,
        Integer estoque,

        MedicationStatus status,
        Long medicineBoxId
) {
}

