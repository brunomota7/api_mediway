package br.com.api_mediway.patient.dto.request;


import br.com.api_mediway.patient.enums.Gender;

import java.time.LocalDate;

public record UpdatePatientInfosDTO(
        String name,
        String email,
        String number,
        LocalDate dateOfBirth,
        Integer age,
        String conditionPatient,
        Gender gender
) {
}
