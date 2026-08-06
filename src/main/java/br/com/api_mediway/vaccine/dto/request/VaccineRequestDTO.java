package br.com.api_mediway.vaccine.dto.request;

import br.com.api_mediway.vaccine.enums.VaccineDoseType;
import br.com.api_mediway.vaccine.enums.VaccineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VaccineRequestDTO(
        @NotBlank String nome,
        @NotNull VaccineDoseType tipoDose,
        LocalDateTime dataVacinou,
        @NotBlank String lote,
        LocalDate dataFabricacao,
        LocalDate proximaDose,
        @NotNull VaccineStatus status
) {}