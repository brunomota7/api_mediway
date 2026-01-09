package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.doctor.AddInfosDoctorDTO;
import br.com.api_mediway.dto.request.doctor.UpdateDoctorInfosDTO;
import br.com.api_mediway.entites.DoctorInfos;
import br.com.api_mediway.entites.User;

import java.util.Optional;

public class DoctorFactory {

    public static DoctorInfos createDoctorInfos(User user, AddInfosDoctorDTO dto) {
        DoctorInfos doctorInfos = new DoctorInfos();
        doctorInfos.setUser(user);
        doctorInfos.setCrm(dto.crm());
        doctorInfos.setSpecialty(dto.specialty());
        doctorInfos.setAvailability(dto.availability());
        return doctorInfos;
    }

    public static void updateDoctorInfos(DoctorInfos doctor, UpdateDoctorInfosDTO dto) {
        Optional.ofNullable(dto.specialty()).filter(s -> !s.isBlank()).ifPresent(doctor::setSpecialty);
        Optional.ofNullable(dto.availability()).ifPresent(doctor::setAvailability);
    }

}
