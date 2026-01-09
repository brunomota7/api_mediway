package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.patient.AddInfosPatientsDTO;
import br.com.api_mediway.dto.request.patient.UpdatePatientInfosDTO;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.entites.User;

import java.util.Optional;

public class PatientFactory {

    public static PatientInfos createPatientInfos(User user, AddInfosPatientsDTO dto) {
        PatientInfos patientInfos = new PatientInfos();
        patientInfos.setUser(user);
        patientInfos.setDateOfBirth(dto.dateOfBirth());
        patientInfos.setAge(dto.age());
        patientInfos.setConditionPatient(dto.conditionPatient());
        patientInfos.setStatusPatient(dto.statusPatient());
        patientInfos.setGender(dto.gender());
        return patientInfos;
    }

    public static void updatePatientInfos(PatientInfos infos, UpdatePatientInfosDTO dto) {
        Optional.ofNullable(dto.dateOfBirth()).ifPresent(infos::setDateOfBirth);
        Optional.ofNullable(dto.age()).ifPresent(infos::setAge);
        Optional.ofNullable(dto.conditionPatient()).filter(s -> !s.isBlank()).ifPresent(infos::setConditionPatient);
        Optional.ofNullable(dto.gender()).ifPresent(infos::setGender);
    }

}
