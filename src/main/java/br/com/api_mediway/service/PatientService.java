package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.patient.AddInfosPatientsDTO;
import br.com.api_mediway.dto.request.patient.UpdatePatientInfosDTO;
import br.com.api_mediway.dto.response.patient.PatientResponseInfosDTO;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.entites.User;
import br.com.api_mediway.enums.ConditionStatusPatient;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.factory.PatientFactory;
import br.com.api_mediway.factory.UserFactory;
import br.com.api_mediway.mapper.PatientMapper;
import br.com.api_mediway.repository.PatientInfosRepository;
import br.com.api_mediway.repository.UserRepository;
import br.com.api_mediway.utils.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PatientService {

    private final UserRepository userRepository;
    private final PatientInfosRepository patientInfosRepository;

    public PatientService(UserRepository userRepository, PatientInfosRepository patientInfosRepository) {
        this.userRepository = userRepository;
        this.patientInfosRepository = patientInfosRepository;
    }

    @Transactional
    public void registerInfosPatient(AddInfosPatientsDTO dto, JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        log.info("[PATIENT] Registering infos for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        PatientInfos patientInfos = PatientFactory.createPatientInfos(user, dto);
        patientInfosRepository.save(patientInfos);

        log.info("[PATIENT] Infos registered successfully for email={}", user.getEmail());
    }

    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public PatientResponseInfosDTO getMyInfos(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        log.info("[PATIENT] Fetching infos for authenticated userId={}", userId);
        return getPatientInfosByUserId(userId);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public PatientResponseInfosDTO getPatientById(UUID patientId) {
        log.info("[PATIENT] Fetching infos by patientId={}", patientId);
        return getPatientInfosByUserId(patientId);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public List<PatientResponseInfosDTO> getAllPatients() {
        log.info("[PATIENT] Fetching all patients");
        List<PatientResponseInfosDTO> patients = patientInfosRepository.findAll()
                .stream()
                .map(PatientMapper::toPatientResponseInfos)
                .toList();

        log.info("[PATIENT] Found {} patients in total", patients.size());
        return patients;
    }

    @Transactional
    public void updateInfosPatient(UpdatePatientInfosDTO dto, JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        log.info("[PATIENT] Updating infos for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        PatientInfos patientInfos = patientInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        UserFactory.updateUserPatient(user, dto);
        PatientFactory.updatePatientInfos(patientInfos, dto);

        userRepository.save(user);
        patientInfosRepository.save(patientInfos);

        log.info("[PATIENT] Infos updated successfully for userId={}", userId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public void updateStatusConditionPatientByUserId(UUID patientId, ConditionStatusPatient statusCondition) {
        log.info("[PATIENT] Updating condition status for patientId={} to {}", patientId, statusCondition);

        PatientInfos patientInfos = patientInfosRepository.findByUserUserId(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patientInfos.setStatusPatient(statusCondition);
        patientInfosRepository.save(patientInfos);

        log.info("[PATIENT] Condition status updated successfully for patientId={}", patientId);
    }

    private PatientResponseInfosDTO getPatientInfosByUserId(UUID userId) {
        PatientInfos patientInfos = patientInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        log.info("[PATIENT] Infos found for userId={}", userId);
        return PatientMapper.toPatientResponseInfos(patientInfos);
    }
}
