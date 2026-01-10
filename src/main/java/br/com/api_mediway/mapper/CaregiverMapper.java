package br.com.api_mediway.mapper;

import br.com.api_mediway.dto.response.caregiver.CaregiverResponseDTO;
import br.com.api_mediway.entites.CaregiverInfos;

import java.util.List;
import java.util.UUID;

public class CaregiverMapper {

    private CaregiverMapper() {}

    public static CaregiverResponseDTO toResponse(CaregiverInfos caregiver) {

        List<UUID> patientIds = caregiver.getPatients()
                .stream()
                .map(p -> p.getUser().getUserId())
                .toList();

        return new CaregiverResponseDTO(
                caregiver.getCaregiverInfoId(),
                caregiver.getUser().getUserId(),
                caregiver.getUser().getName(),
                caregiver.getUser().getEmail(),
                caregiver.getSpecialty(),
                caregiver.getExperience(),
                caregiver.getAvailability(),
                patientIds
        );
    }
}

