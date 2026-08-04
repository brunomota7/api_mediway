package br.com.api_mediway.vaccine.dto.response;

import br.com.api_mediway.vaccine.enums.VaccineDoseType;
import br.com.api_mediway.vaccine.enums.VaccineStatus;

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
