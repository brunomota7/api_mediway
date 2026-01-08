package br.com.api_mediway.dto.request.patient;

import br.com.mediway_api.enums.Gender;

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
