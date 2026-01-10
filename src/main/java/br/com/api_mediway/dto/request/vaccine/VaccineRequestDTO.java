package br.com.api_mediway.dto.request.vaccine;

import br.com.api_mediway.enums.VaccineDoseType;
import br.com.api_mediway.enums.VaccineStatus;

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
