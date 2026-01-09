package br.com.api_mediway.controller;

import br.com.api_mediway.dto.request.patient.AddInfosPatientsDTO;
import br.com.api_mediway.dto.request.patient.UpdatePatientInfosDTO;
import br.com.api_mediway.dto.response.patient.PatientResponseInfosDTO;
import br.com.api_mediway.enums.ConditionStatusPatient;
import br.com.api_mediway.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // cadastra as informações do paciente autenticado
    @PostMapping("/add-infos")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> registerInfosPatient(
            @RequestBody AddInfosPatientsDTO dto,
            JwtAuthenticationToken token
    ) {
        log.info("[PATIENT] POST /patients/add-infos - Starting patient information registration");
        patientService.registerInfosPatient(dto, token);
        log.info("[PATIENT] POST /patients/add-infos - Patient information successfully registered");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // retorna as informações do paciente autenticado
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<PatientResponseInfosDTO> getMyInfos(JwtAuthenticationToken token) {
        log.info("[PATIENT] GET /patients/me - Fetching authenticated patient infos");
        var response = patientService.getMyInfos(token);
        log.info("[PATIENT] GET /patients/me - Infos fetched successfully for authenticated user");
        return ResponseEntity.ok(response);
    }

    // retorna informações de um paciente pelo ID
    @GetMapping("/{idPatient}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<PatientResponseInfosDTO> getPatientById(@PathVariable UUID idPatient) {
        log.info("[PATIENT] GET /patients/{} - Fetching patient by ID", idPatient);
        var response = patientService.getPatientById(idPatient);
        log.info("[PATIENT] GET /patients/{} - Patient found successfully", idPatient);
        return ResponseEntity.ok(response);
    }

    // lista todos os pacientes do sistema
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<List<PatientResponseInfosDTO>> getAllPatients() {
        log.info("[PATIENT] GET /patients - Fetching all patients");
        var listPatients = patientService.getAllPatients();
        log.info("[PATIENT] GET /patients - Found {} patients", listPatients.size());
        return ResponseEntity.ok(listPatients);
    }

    // atualiza as informações do paciente autenticado
    @PutMapping("/update-infos")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> updateInfosPatient(
            @RequestBody UpdatePatientInfosDTO dto,
            JwtAuthenticationToken token
    ) {
        log.info("[PATIENT] PUT /patients/update-infos - Updating patient infos");
        patientService.updateInfosPatient(dto, token);
        log.info("[PATIENT] PUT /patients/update-infos - Infos updated successfully");
        return ResponseEntity.ok().build();
    }

    // atualiza o status da condição de um paciente
    @PatchMapping("/{patientId}/status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> updatePatientStatus(
            @PathVariable UUID patientId,
            @RequestParam ConditionStatusPatient statusCondition
    ) {
        log.info("[PATIENT] PATCH /patients/{}/status - Updating condition status to {}", patientId, statusCondition);
        patientService.updateStatusConditionPatientByUserId(patientId, statusCondition);
        log.info("[PATIENT] PATCH /patients/{}/status - Condition status updated successfully", patientId);
        return ResponseEntity.noContent().build();
    }
}
