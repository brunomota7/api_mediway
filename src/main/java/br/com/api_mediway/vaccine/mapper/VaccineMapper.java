package br.com.api_mediway.vaccine.mapper;

import br.com.api_mediway.vaccine.dto.response.VaccineResponseDTO;
import br.com.api_mediway.vaccine.entity.Vaccine;
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

