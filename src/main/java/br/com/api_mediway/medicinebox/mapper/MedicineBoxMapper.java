package br.com.api_mediway.medicinebox.mapper;

import br.com.api_mediway.medicinebox.dto.response.MedicineBoxResponseDTO;
import br.com.api_mediway.medication.entity.Medication;
import br.com.api_mediway.medication.mapper.MedicationMapper;
import br.com.api_mediway.medicinebox.entity.MedicineBox;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MedicineBoxMapper {

    public static MedicineBoxResponseDTO toResponse(MedicineBox box) {

        Map<String, List<Medication>> agrupadoPorGaveta =
                box.getMedications().stream()
                        .collect(Collectors.groupingBy(Medication::getGaveta));

        List<MedicineBoxResponseDTO.GavetaDTO> gavetas =
                agrupadoPorGaveta.entrySet().stream()
                        .map(entry -> new MedicineBoxResponseDTO.GavetaDTO(
                                entry.getKey(),
                                entry.getValue().stream()
                                        .map(MedicationMapper::toResponse)
                                        .toList()
                        ))
                        .toList();

        return new MedicineBoxResponseDTO(
                box.getMedicineBoxId(),
                box.getNome(),
                gavetas
        );
    }
}

