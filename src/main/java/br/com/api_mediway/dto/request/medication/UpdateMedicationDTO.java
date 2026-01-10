package br.com.api_mediway.dto.request.medication;

import br.com.api_mediway.enums.MedicationStatus;

import java.time.LocalTime;
import java.util.List;

public record UpdateMedicationDTO(
        String descricao,
        List<String> dias,
        LocalTime hora,
        Integer estoque,
        MedicationStatus status

) {
}
