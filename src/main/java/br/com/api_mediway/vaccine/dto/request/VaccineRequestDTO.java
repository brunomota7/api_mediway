package br.com.api_mediway.vaccine.dto.request;

import br.com.api_mediway.vaccine.enums.VaccineDoseType;
import br.com.api_mediway.vaccine.enums.VaccineStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VaccineRequestDTO(
        String nome,
        VaccineDoseType tipoDose,
        LocalDateTime dataVacinou,
        String lote,
        LocalDate dataFabricacao,
        LocalDate proximaDose,
        VaccineStatus status
) {}
