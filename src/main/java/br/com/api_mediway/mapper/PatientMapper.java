package br.com.api_mediway.mapper;


import br.com.api_mediway.dto.response.patient.PatientResponseInfosDTO;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.entites.Role;

import java.util.List;

public class PatientMapper {

    public static PatientResponseInfosDTO toPatientResponseInfos(PatientInfos patient) {
        var user = patient.getUser();

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        return new PatientResponseInfosDTO(
                user.getUserId(),
                new PatientResponseInfosDTO.PersonalInfoDTO(
                        user.getName(),
                        patient.getDateOfBirth(),
                        patient.getAge(),
                        patient.getGender().getLabel(),
                        roles
                ),
                new PatientResponseInfosDTO.ContactInfoDTO(
                        user.getEmail(),
                        user.getNumber()
                ),
                new PatientResponseInfosDTO.MedicalInfoDTO(
                        patient.getConditionPatient(),
                        patient.getStatusPatient().getLabel()
                )
        );
    }
}
