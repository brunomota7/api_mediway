package br.com.api_mediway.caregiver.factory;

import br.com.api_mediway.caregiver.dto.request.CaregiverRequestDTO;
import br.com.api_mediway.caregiver.entity.CaregiverInfos;
import br.com.api_mediway.user.entity.User;
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

