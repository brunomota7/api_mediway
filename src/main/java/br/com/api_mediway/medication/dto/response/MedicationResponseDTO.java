package br.com.api_mediway.medication.dto.response;

import br.com.api_mediway.medication.enums.MedicationStatus;
import br.com.api_mediway.medication.enums.MedicationType;

import java.time.LocalTime;
import java.util.List;

public record MedicationResponseDTO(

        Long medicationId,
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

        MedicationStatus status

) {
}
