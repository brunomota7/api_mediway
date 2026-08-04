package br.com.api_mediway.doctor.mapper;


import br.com.api_mediway.doctor.dto.response.DoctorResponseDTO;
import br.com.api_mediway.doctor.entity.DoctorInfos;
import br.com.api_mediway.user.entity.Role;

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
