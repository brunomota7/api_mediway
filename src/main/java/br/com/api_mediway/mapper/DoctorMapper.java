package br.com.api_mediway.mapper;


import br.com.api_mediway.dto.response.doctor.DoctorResponseDTO;
import br.com.api_mediway.entites.DoctorInfos;
import br.com.api_mediway.entites.Role;

import java.util.List;

public class DoctorMapper {

    public static DoctorResponseDTO toResponse(DoctorInfos doctor) {
        var user = doctor.getUser();

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        return new DoctorResponseDTO(
                user.getUserId(),
                new DoctorResponseDTO.PersonalInfoDTO(
                        user.getName(),
                        roles
                ),
                new DoctorResponseDTO.ContactInfoDTO(
                        user.getEmail(),
                        user.getNumber()
                ),
                new DoctorResponseDTO.ProfessionalInfoDTO(
                        doctor.getCrm(),
                        doctor.getSpecialty(),
                        doctor.getAvailability()
                )
        );
    }
}
