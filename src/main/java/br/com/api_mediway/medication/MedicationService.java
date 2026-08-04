package br.com.api_mediway.medication;

import br.com.api_mediway.medication.dto.request.MedicationRequestDTO;
import br.com.api_mediway.medication.dto.response.MedicationResponseDTO;
import br.com.api_mediway.medication.entity.Medication;
import br.com.api_mediway.medicinebox.entity.MedicineBox;
import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.medication.enums.MedicationStatus;
import br.com.api_mediway.common.exception.UserNotFoundException;
import br.com.api_mediway.medication.factory.MedicationFactory;
import br.com.api_mediway.medication.mapper.MedicationMapper;
import br.com.api_mediway.medication.repository.MedicationRepository;
import br.com.api_mediway.medicinebox.repository.MedicineBoxRepository;
import br.com.api_mediway.patient.repository.PatientInfosRepository;
import br.com.api_mediway.common.util.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final PatientInfosRepository patientInfosRepository;
    private final MedicineBoxRepository medicineBoxRepository;
    private final MedicationFactory medicationFactory;

    public MedicationService(
            MedicationRepository medicationRepository,
            PatientInfosRepository patientInfosRepository,
            MedicineBoxRepository medicineBoxRepository,
            MedicationFactory medicationFactory
    ) {
        this.medicationRepository = medicationRepository;
        this.patientInfosRepository = patientInfosRepository;
        this.medicineBoxRepository = medicineBoxRepository;
        this.medicationFactory = medicationFactory;
    }

    @Transactional
    public void registerMedication(UUID patientUserId, MedicationRequestDTO dto) {
        log.info("[MEDICATION] Registering medication for patient userId={}", patientUserId);

        PatientInfos patient = patientInfosRepository.findByUserUserId(patientUserId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        MedicineBox medicineBox = medicineBoxRepository.findById(dto.medicineBoxId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Medicine box not found"));

        Medication medication = medicationFactory.create(dto, patient, medicineBox);

        medicationRepository.save(medication);

        log.info("[MEDICATION] Medication registered successfully for patient userId={}", patientUserId);
    }

    public List<MedicationResponseDTO> getMyMedications(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);

        log.info("[MEDICATION] Fetching medications for userId={}", userId);

        return medicationRepository.findByPatientInfosUserUserId(userId)
                .stream()
                .map(MedicationMapper::toResponse)
                .toList();
    }

    public MedicationResponseDTO getMedicationById(Long medicationId) {
        log.info("[MEDICATION] Fetching medication id={}", medicationId);

        Medication medication = findMedicationById(medicationId);
        return MedicationMapper.toResponse(medication);
    }

    @Transactional
    public void updateMedicationStatus(Long medicationId, MedicationStatus status) {
        log.info("[MEDICATION] Updating medication status id={} to {}", medicationId, status);

        Medication medication = findMedicationById(medicationId);
        medication.setStatus(status);

        medicationRepository.save(medication);
    }

    @Transactional
    public void deleteMedication(Long medicationId) {
        log.warn("[MEDICATION] Deleting medication id={}", medicationId);

        Medication medication = findMedicationById(medicationId);
        medicationRepository.delete(medication);
    }

    /*
     * Métodos auxiliares
     */

    private Medication findMedicationById(Long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Medication not found"));
    }
}
