package br.com.api_mediway.vaccine;

import br.com.api_mediway.vaccine.dto.request.VaccineRequestDTO;
import br.com.api_mediway.vaccine.dto.response.VaccineResponseDTO;
import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.vaccine.entity.Vaccine;
import br.com.api_mediway.vaccine.enums.VaccineStatus;
import br.com.api_mediway.vaccine.factory.VaccineFactory;
import br.com.api_mediway.vaccine.mapper.VaccineMapper;
import br.com.api_mediway.patient.repository.PatientInfosRepository;
import br.com.api_mediway.vaccine.repository.VaccineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final PatientInfosRepository patientInfosRepository;
    private final VaccineFactory vaccineFactory;
    private final VaccineMapper vaccineMapper;

    public VaccineService(
            VaccineRepository vaccineRepository,
            PatientInfosRepository patientInfosRepository,
            VaccineFactory vaccineFactory,
            VaccineMapper vaccineMapper
    ) {
        this.vaccineRepository = vaccineRepository;
        this.patientInfosRepository = patientInfosRepository;
        this.vaccineFactory = vaccineFactory;
        this.vaccineMapper = vaccineMapper;
    }

    // registrar vacina
    public void registerVaccine(UUID patientId, VaccineRequestDTO dto) {
        log.info("[VACCINE] Registering vaccine for patientId={}", patientId);

        PatientInfos patient = patientInfosRepository.findByUserUserId(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Vaccine vaccine = vaccineFactory.create(dto, patient);
        vaccineRepository.save(vaccine);

        log.info("[VACCINE] Vaccine registered successfully for patientId={}", patientId);
    }

    // vacinas do paciente
    @Transactional
    public List<VaccineResponseDTO> getVaccinesByPatient(UUID patientId) {
        log.info("[VACCINE] Fetching vaccines for patientId={}", patientId);

        List<Vaccine> vaccines = vaccineRepository.findByPatientInfosUserUserId(patientId);
        return vaccineMapper.toResponseList(vaccines);
    }

    // vacina por ID
    @Transactional
    public VaccineResponseDTO getVaccineById(Long vaccineId) {
        log.info("[VACCINE] Fetching vaccine id={}", vaccineId);

        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine not found"));

        return vaccineMapper.toResponse(vaccine);
    }

    // vacinas por status
    @Transactional
    public List<VaccineResponseDTO> getVaccinesByStatus(VaccineStatus status) {
        log.info("[VACCINE] Fetching vaccines by status={}", status);

        List<Vaccine> vaccines = vaccineRepository.findByStatus(status);
        return vaccineMapper.toResponseList(vaccines);
    }

    // atualizar status
    public void updateVaccineStatus(Long vaccineId, VaccineStatus status) {
        log.info("[VACCINE] Updating vaccine status id={}, newStatus={}", vaccineId, status);

        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine not found"));

        vaccine.setStatus(status);
        vaccineRepository.save(vaccine);
    }

    // deletar vacina
    public void deleteVaccine(Long vaccineId) {
        log.warn("[VACCINE] Deleting vaccine id={}", vaccineId);

        if (!vaccineRepository.existsById(vaccineId)) {
            throw new EntityNotFoundException("Vaccine not found");
        }

        vaccineRepository.deleteById(vaccineId);
    }
}
