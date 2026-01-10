package br.com.api_mediway.mapper;

import br.com.api_mediway.dto.response.vaccine.VaccineResponseDTO;
import br.com.api_mediway.entites.Vaccine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VaccineMapper {

    public VaccineResponseDTO toResponse(Vaccine vaccine) {
        return new VaccineResponseDTO(
                vaccine.getVaccineId(),
                vaccine.getNome(),
                vaccine.getTipoDose(),
                vaccine.getDataVacinou(),
                vaccine.getLote(),
                vaccine.getDataFabricacao(),
                vaccine.getProximaDose(),
                vaccine.getStatus(),
                vaccine.getPatientInfos().getUser().getUserId()
        );
    }

    public List<VaccineResponseDTO> toResponseList(List<Vaccine> vaccines) {
        return vaccines.stream()
                .map(this::toResponse)
                .toList();
    }
}

