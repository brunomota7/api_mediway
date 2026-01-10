package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.medicineBox.MedicineBoxRequestDTO;
import br.com.api_mediway.dto.response.medicineBox.MedicineBoxResponseDTO;
import br.com.api_mediway.entites.Medication;
import br.com.api_mediway.entites.MedicineBox;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.factory.MedicineBoxFactory;
import br.com.api_mediway.mapper.MedicineBoxMapper;
import br.com.api_mediway.repository.MedicationRepository;
import br.com.api_mediway.repository.MedicineBoxRepository;
import br.com.api_mediway.repository.PatientInfosRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MedicineBoxService {

    private final MedicineBoxRepository medicineBoxRepository;
    private final MedicationRepository medicationRepository;
    private final PatientInfosRepository patientInfosRepository;

    public MedicineBoxService(
            MedicineBoxRepository medicineBoxRepository,
            MedicationRepository medicationRepository,
            PatientInfosRepository patientInfosRepository
    ) {
        this.medicineBoxRepository = medicineBoxRepository;
        this.medicationRepository = medicationRepository;
        this.patientInfosRepository = patientInfosRepository;
    }

    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public MedicineBoxResponseDTO getMyMedicineBox(UUID userId) {
        MedicineBox box = findByUserId(userId);
        return MedicineBoxMapper.toResponse(box);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MEDICO') or hasAuthority('SCOPE_CUIDADOR')")
    public MedicineBoxResponseDTO getMedicineBoxByUser(UUID userId) {
        MedicineBox box = findByUserId(userId);
        return MedicineBoxMapper.toResponse(box);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public void updateMedicineBoxName(UUID userId, String nome) {
        MedicineBox box = findByUserId(userId);
        box.setNome(nome);
        medicineBoxRepository.save(box);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public void deleteMedication(Long medicationId) {
        medicationRepository.deleteById(medicationId);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public void deleteGaveta(UUID userId, String gaveta) {
        MedicineBox box = findByUserId(userId);
        List<Medication> meds =
                medicationRepository.findByMedicineBoxMedicineBoxIdAndGaveta(
                        box.getMedicineBoxId(), gaveta
                );
        medicationRepository.deleteAll(meds);
    }

    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteMedicineBox(UUID userId) {
        MedicineBox box = findByUserId(userId);
        medicineBoxRepository.delete(box);
    }

    private MedicineBox findByUserId(UUID userId) {
        return medicineBoxRepository.findByPatientInfosUserUserId(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "MedicineBox not found")
                );
    }
}

