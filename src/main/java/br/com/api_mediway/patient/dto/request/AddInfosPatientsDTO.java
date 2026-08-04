package br.com.api_mediway.patient.dto.request;

import br.com.api_mediway.patient.enums.ConditionStatusPatient;
import br.com.api_mediway.patient.enums.Gender;
import java.time.LocalDate;

public record AddInfosPatientsDTO(
        LocalDate dateOfBirth,
        Integer age,
        String conditionPatient,
        ConditionStatusPatient statusPatient,
        Gender gender
) {
}
