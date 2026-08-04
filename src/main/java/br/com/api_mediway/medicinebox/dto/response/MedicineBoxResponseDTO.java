package br.com.api_mediway.medicinebox.dto.response;

import br.com.api_mediway.medication.dto.response.MedicationResponseDTO;

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

