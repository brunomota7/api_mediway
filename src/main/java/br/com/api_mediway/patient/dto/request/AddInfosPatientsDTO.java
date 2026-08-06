package br.com.api_mediway.patient.dto.request;

import br.com.api_mediway.patient.enums.ConditionStatusPatient;
import br.com.api_mediway.patient.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record AddInfosPatientsDTO(
        @NotNull @Past LocalDate dateOfBirth,
        @PositiveOrZero Integer age,
        @NotBlank String conditionPatient,
        @NotNull ConditionStatusPatient statusPatient,
        @NotNull Gender gender
) {
}