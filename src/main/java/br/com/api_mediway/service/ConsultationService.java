package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.consultation.ConsultationRequestDTO;
import br.com.api_mediway.dto.request.consultation.UpdateConsultationDTO;
import br.com.api_mediway.dto.response.consultation.ConsultationResponseDTO;
import br.com.api_mediway.entites.Consultation;
import br.com.api_mediway.entites.DoctorInfos;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.enums.ConsultationAndExmStatus;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.factory.ConsultationFactory;
import br.com.api_mediway.mapper.ConsultationMapper;
import br.com.api_mediway.repository.ConsultationRepository;
import br.com.api_mediway.repository.DoctorInfosRepository;
import br.com.api_mediway.repository.PatientInfosRepository;
import br.com.api_mediway.utils.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final PatientInfosRepository patientInfosRepository;
    private final DoctorInfosRepository doctorInfosRepository;

    public ConsultationService(ConsultationRepository consultationRepository,
                               PatientInfosRepository patientInfosRepository,
                               DoctorInfosRepository doctorInfosRepository) {
        this.consultationRepository = consultationRepository;
        this.patientInfosRepository = patientInfosRepository;
        this.doctorInfosRepository = doctorInfosRepository;
    }

    @Transactional
    public void scheduleConsultation(UUID patientId, ConsultationRequestDTO dto) {
        log.info("[CONSULTATION] Scheduling new consultation for patient Id={}", patientId);

        PatientInfos patient = findPatientByUserId(patientId);
        DoctorInfos doctor = findDoctorByUserId(dto.doctorId());

        Consultation consultation = ConsultationFactory.registerConsultation(patient, doctor, dto);

        consultationRepository.save(consultation);

        log.info("[CONSULTATION] Consultation scheduled successfully for patientId={}", patientId);
    }

    @PreAuthorize("hasAuthority('SCOPE_MEDICO') or hasAuthority('SCOPE_PACIENTE')")
    public List<ConsultationResponseDTO> getUserConsultations(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        boolean isDoctor = token.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("SCOPE_MEDICO"));
        boolean isPatient = token.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("SCOPE_PACIENTE"));

        log.info("[CONSULTATION] Fetching consultations for userId={} (role: {})",
                userId, isDoctor ? "MEDICO" : "PACIENTE");

        List<Consultation> consultations;
        if (isDoctor) {
            consultations = consultationRepository.findByDoctorInfosUserUserId(userId);
        } else if (isPatient) {
            consultations = consultationRepository.findByPatientInfosUserUserId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User role not allowed for this operation");
        }

        log.info("[CONSULTATION] Found {} consultations for userId={}", consultations.size(), userId);

        return consultations.stream()
                .map(ConsultationMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public List<ConsultationResponseDTO> getConsultationsByUserId(UUID userId) {
        log.info("[CONSULTATION] Fetching consultations for userId={}", userId);

        boolean isDoctor = doctorInfosRepository.findByUserUserId(userId).isPresent();
        boolean isPatient = patientInfosRepository.findByUserUserId(userId).isPresent();

        if (!isDoctor && !isPatient) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor or patient found for the given userId");
        }

        List<Consultation> consultations;

        if (isDoctor) {
            consultations = consultationRepository.findByDoctorInfosUserUserId(userId);
            log.info("[CONSULTATION] Found {} consultations for doctor userId={}", consultations.size(), userId);
        } else {
            consultations = consultationRepository.findByPatientInfosUserUserId(userId);
            log.info("[CONSULTATION] Found {} consultations for patient userId={}", consultations.size(), userId);
        }

        return consultations.stream()
                .map(ConsultationMapper::toResponse)
                .toList();
    }

    public ConsultationResponseDTO getConsultationById(Long consultationId) {
        log.info("[CONSULTATION] Fetching consultation id={}", consultationId);
        Consultation consultation = findConsultationById(consultationId);
        return ConsultationMapper.toResponse(consultation);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public List<ConsultationResponseDTO> getConsultationsByDate(LocalDate date) {
        log.info("[CONSULTATION] Fetching consultations for date={}", date);
        return consultationRepository.findByConsultationDate(date)
                .stream()
                .map(ConsultationMapper::toResponse)
                .toList();
    }

    public List<ConsultationResponseDTO> getConsultationByStatus(ConsultationAndExmStatus status) {
        log.info("[CONSULTATION] Fetching consultations for status={}", status);
        return consultationRepository.findByStatus(status)
                .stream()
                .map(ConsultationMapper::toResponse)
                .toList();
    }

    @Transactional
    public void updateConsultationsInfo(Long consultationId, UpdateConsultationDTO dto) {
        log.info("[CONSULTATION] Updating consultation info id={}", consultationId);

        Consultation consultation = findConsultationById(consultationId);

        if (dto.description() != null && !dto.description().isBlank())
            consultation.setDescription(dto.description());

        if (dto.localConsultation() != null && !dto.localConsultation().isBlank())
            consultation.setLocalConsultation(dto.localConsultation());

        if (dto.requeriments() != null && !dto.requeriments().isBlank())
            consultation.setRequeriments(dto.requeriments());

        consultationRepository.save(consultation);
        log.info("[CONSULTATION] Consultation updated successfully id={}", consultationId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public void updateConsultationStatus(Long consultationId, ConsultationAndExmStatus status) {
        log.info("[CONSULTATION] Updating consultation status id={}", consultationId);

        Consultation consultation = findConsultationById(consultationId);
        consultation.setStatus(status);

        consultationRepository.save(consultation);
        log.info("[CONSULTATION] Consultation status updated successfully id={}", consultationId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public void deleteConsultation(Long consultationId) {
        log.warn("[CONSULTATION] Deleting consultation id={}", consultationId);

        Consultation consultation = findConsultationById(consultationId);
        consultationRepository.delete(consultation);

        log.info("[CONSULTATION] Consultation deleted successfully id={}", consultationId);
    }

    /*
    * Métodos auxiliares
    */

    private Consultation findConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consultation not found"));
    }

    private PatientInfos findPatientByUserId(UUID userId) {
        return patientInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
    }

    private DoctorInfos findDoctorByUserId(UUID userId) {
        return doctorInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));
    }
}
