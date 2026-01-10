package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.caregiver.CaregiverRequestDTO;
import br.com.api_mediway.dto.response.caregiver.CaregiverResponseDTO;
import br.com.api_mediway.entites.CaregiverInfos;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.entites.User;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.factory.CaregiverFactory;
import br.com.api_mediway.mapper.CaregiverMapper;
import br.com.api_mediway.repository.CaregiverInfosRepository;
import br.com.api_mediway.repository.PatientInfosRepository;
import br.com.api_mediway.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@Service
public class CaregiverService {

    private final CaregiverInfosRepository caregiverRepository;
    private final UserRepository userRepository;
    private final PatientInfosRepository patientInfosRepository;
    private final CaregiverFactory caregiverFactory;

    public CaregiverService(
            CaregiverInfosRepository caregiverRepository,
            UserRepository userRepository,
            PatientInfosRepository patientInfosRepository,
            CaregiverFactory caregiverFactory
    ) {
        this.caregiverRepository = caregiverRepository;
        this.userRepository = userRepository;
        this.patientInfosRepository = patientInfosRepository;
        this.caregiverFactory = caregiverFactory;
    }

    @Transactional
    public void registerCaregiver(UUID userId, CaregiverRequestDTO dto) {

        log.info("[CAREGIVER] Registering caregiver for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (caregiverRepository.existsByUserUserId(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Caregiver already registered for this user");
        }

        CaregiverInfos caregiver = caregiverFactory.create(user, dto);
        caregiverRepository.save(caregiver);

        log.info("[CAREGIVER] Caregiver registered successfully userId={}", userId);
    }

    public CaregiverResponseDTO getCaregiverByUserId(UUID userId) {

        log.info("[CAREGIVER] Fetching caregiver by userId={}", userId);

        CaregiverInfos caregiver = caregiverRepository.findByUserUserId(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Caregiver not found"));

        return CaregiverMapper.toResponse(caregiver);
    }

    @Transactional
    public void assignPatient(Long caregiverId, UUID patientUserId) {

        log.info("[CAREGIVER] Assigning patient userId={} to caregiverId={}", patientUserId, caregiverId);

        CaregiverInfos caregiver = findCaregiverById(caregiverId);

        PatientInfos patient = patientInfosRepository.findByUserUserId(patientUserId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        caregiver.getPatients().add(patient);

        caregiverRepository.save(caregiver);
    }

    @Transactional
    public void removePatient(Long caregiverId, UUID patientUserId) {

        log.info("[CAREGIVER] Removing patient userId={} from caregiverId={}", patientUserId, caregiverId);

        CaregiverInfos caregiver = findCaregiverById(caregiverId);

        caregiver.getPatients().removeIf(
                p -> p.getUser().getUserId().equals(patientUserId)
        );

        caregiverRepository.save(caregiver);
    }

    @Transactional
    public void deleteCaregiver(Long caregiverId) {

        log.warn("[CAREGIVER] Deleting caregiver id={}", caregiverId);

        CaregiverInfos caregiver = findCaregiverById(caregiverId);
        caregiverRepository.delete(caregiver);
    }

    /*
     * Métodos auxiliares
     */

    private CaregiverInfos findCaregiverById(Long caregiverId) {
        return caregiverRepository.findById(caregiverId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Caregiver not found"));
    }
}
