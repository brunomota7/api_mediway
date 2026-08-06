package br.com.api_mediway.patient.dto.request;


import br.com.api_mediway.patient.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record UpdatePatientInfosDTO(
        String name,
        @Email String email,
        String number,
        @Past LocalDate dateOfBirth,
        @PositiveOrZero Integer age,
        String conditionPatient,
        Gender gender
) {
}