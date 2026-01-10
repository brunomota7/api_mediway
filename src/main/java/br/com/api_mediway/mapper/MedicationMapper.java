package br.com.api_mediway.mapper;

import br.com.api_mediway.dto.response.medication.MedicationResponseDTO;
import br.com.api_mediway.entites.Medication;

public class MedicationMapper {

    public static MedicationResponseDTO toResponse(Medication medication) {

        return new MedicationResponseDTO(
                medication.getMedicationId(),
                medication.getNome(),
                medication.getTipo(),
                medication.getNomeReferencia(),
                medication.getDescricao(),
                medication.getConcentracao(),
                medication.getQuantidade(),
                medication.getDias(),
                medication.getHora(),
                medication.getGaveta(),
                medication.getEstoque(),
                medication.getStatus()
        );
    }
}

