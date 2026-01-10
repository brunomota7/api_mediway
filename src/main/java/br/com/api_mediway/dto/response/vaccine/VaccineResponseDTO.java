package br.com.api_mediway.dto.response.vaccine;

import br.com.api_mediway.enums.VaccineDoseType;
import br.com.api_mediway.enums.VaccineStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VaccineResponseDTO(
        Long vaccineId,
        String nome,
        VaccineDoseType tipoDose,
        LocalDateTime dataVacinou,
        String lote,
        LocalDate dataFabricacao,
        LocalDate proximaDose,
        VaccineStatus status,
        UUID patientId
) {}
