package br.com.api_mediway.dto.request.patient;

import br.com.api_mediway.enums.ConditionStatusPatient;
import br.com.api_mediway.enums.Gender;
import java.time.LocalDate;

public record AddInfosPatientsDTO(
        LocalDate dateOfBirth,
        Integer age,
        String conditionPatient,
        ConditionStatusPatient statusPatient,
        Gender gender
) {
}
