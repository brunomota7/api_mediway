package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.caregiver.CaregiverRequestDTO;
import br.com.api_mediway.entites.CaregiverInfos;
import br.com.api_mediway.entites.User;
import org.springframework.stereotype.Component;

@Component
public class CaregiverFactory {

    public CaregiverInfos create(User user, CaregiverRequestDTO dto) {

        CaregiverInfos caregiver = new CaregiverInfos();
        caregiver.setUser(user);
        caregiver.setSpecialty(dto.specialty());
        caregiver.setExperience(dto.experience());
        caregiver.setAvailability(dto.availability());

        return caregiver;
    }
}

