package br.com.api_mediway.user.factory;

import br.com.api_mediway.doctor.dto.request.UpdateDoctorInfosDTO;
import br.com.api_mediway.patient.dto.request.UpdatePatientInfosDTO;
import br.com.api_mediway.user.entity.User;

import java.util.Optional;

public class UserFactory {

    public static void updateUserPatient(User user, UpdatePatientInfosDTO dto) {
        Optional.ofNullable(dto.name()).filter(s -> !s.isBlank()).ifPresent(user::setName);
        Optional.ofNullable(dto.email()).filter(s -> !s.isBlank()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.number()).filter(s -> !s.isBlank()).ifPresent(user::setNumber);
    }

    public static void updateUserDoctor(User user, UpdateDoctorInfosDTO dto) {
        Optional.ofNullable(dto.name()).filter(s -> !s.isBlank()).ifPresent(user::setName);
        Optional.ofNullable(dto.email()).filter(s -> !s.isBlank()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.number()).filter(s -> !s.isBlank()).ifPresent(user::setNumber);
        Optional.ofNullable(dto.imageUrl()).filter(s -> !s.isBlank()).ifPresent(user::setImageUrl);
    }

}
