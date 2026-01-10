package br.com.api_mediway.dto.response.medicineBox;

import br.com.api_mediway.dto.response.medication.MedicationResponseDTO;

import java.util.List;

public record MedicineBoxResponseDTO(
        Long medicineBoxId,
        String nome,
        List<GavetaDTO> gavetas
) {
    public record GavetaDTO(
            String nome,
            List<MedicationResponseDTO> medicamentos
    ) {}
}

