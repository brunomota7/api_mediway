package br.com.api_mediway.controller;

import br.com.api_mediway.dto.request.vaccine.VaccineRequestDTO;
import br.com.api_mediway.dto.response.vaccine.VaccineResponseDTO;
import br.com.api_mediway.enums.VaccineStatus;
import br.com.api_mediway.service.VaccineService;
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
@RequestMapping("/vaccine")
public class VaccineController {

    private final VaccineService vaccineService;

    public VaccineController(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    // registrar vacina para um paciente
    @PostMapping("/register/{patientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> registerVaccine(
            @PathVariable UUID patientId,
            @RequestBody VaccineRequestDTO dto
    ) {
        log.info("[VACCINE] POST /vaccine/register/{} - Registering vaccine", patientId);
        vaccineService.registerVaccine(patientId, dto);
        log.info("[VACCINE] Vaccine registered successfully for patientId={}", patientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // vacinas do paciente logado
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<List<VaccineResponseDTO>> getMyVaccines(JwtAuthenticationToken token) {
        log.info("[VACCINE] GET /vaccine/me - Fetching vaccines for logged patient");

        UUID patientId = UUID.fromString(token.getName());
        List<VaccineResponseDTO> vaccines = vaccineService.getVaccinesByPatient(patientId);

        log.info("[VACCINE] Found {} vaccines for patientId={}", vaccines.size(), patientId);
        return ResponseEntity.ok(vaccines);
    }

    // vacinas por ID do paciente
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<List<VaccineResponseDTO>> getVaccinesByPatient(
            @PathVariable UUID patientId
    ) {
        log.info("[VACCINE] GET /vaccine/patient/{} - Fetching vaccines", patientId);

        List<VaccineResponseDTO> vaccines = vaccineService.getVaccinesByPatient(patientId);

        log.info("[VACCINE] Found {} vaccines for patientId={}", vaccines.size(), patientId);
        return ResponseEntity.ok(vaccines);
    }

    // vacina específica por ID
    @GetMapping("/{vaccineId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_PACIENTE') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<VaccineResponseDTO> getVaccineById(
            @PathVariable Long vaccineId
    ) {
        log.info("[VACCINE] GET /vaccine/{} - Fetching vaccine by ID", vaccineId);

        VaccineResponseDTO response = vaccineService.getVaccineById(vaccineId);

        log.info("[VACCINE] Vaccine fetched successfully id={}", vaccineId);
        return ResponseEntity.ok(response);
    }

    // vacinas por status
    @GetMapping("/by-status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<List<VaccineResponseDTO>> getVaccinesByStatus(
            @RequestParam("status") VaccineStatus status
    ) {
        log.info("[VACCINE] GET /vaccine/by-status?status={} - Fetching vaccines", status);

        List<VaccineResponseDTO> vaccines = vaccineService.getVaccinesByStatus(status);

        log.info("[VACCINE] Found {} vaccines for status={}", vaccines.size(), status);
        return ResponseEntity.ok(vaccines);
    }

    // atualizar status da vacina
    @PutMapping("/{vaccineId}/status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> updateVaccineStatus(
            @PathVariable Long vaccineId,
            @RequestParam VaccineStatus status
    ) {
        log.info("[VACCINE] PUT /vaccine/{}/status - Changing status to {}", vaccineId, status);

        vaccineService.updateVaccineStatus(vaccineId, status);

        log.info("[VACCINE] Vaccine status updated successfully id={}, newStatus={}", vaccineId, status);
        return ResponseEntity.noContent().build();
    }

    // deletar vacina
    @DeleteMapping("/{vaccineId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> deleteVaccine(
            @PathVariable Long vaccineId
    ) {
        log.warn("[VACCINE] DELETE /vaccine/{} - Deleting vaccine", vaccineId);

        vaccineService.deleteVaccine(vaccineId);

        log.info("[VACCINE] Vaccine deleted successfully id={}", vaccineId);
        return ResponseEntity.noContent().build();
    }
}
