package br.com.api_mediway.controller;

import br.com.api_mediway.dto.request.medication.MedicationRequestDTO;
import br.com.api_mediway.dto.response.medication.MedicationResponseDTO;
import br.com.api_mediway.enums.MedicationStatus;
import br.com.api_mediway.service.MedicationService;
import jakarta.validation.Valid;
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
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping("/user/{patientUserId}")
    @PreAuthorize("""
        hasAuthority('SCOPE_PACIENTE') 
        or hasAuthority('SCOPE_MEDICO') 
        or hasAuthority('SCOPE_CUIDADOR')
    """)
    public ResponseEntity<Void> registerMedication(
            @PathVariable UUID patientUserId,
            @RequestBody @Valid MedicationRequestDTO dto
    ) {
        log.info("[MEDICATION_CONTROLLER] Register medication for patientUserId={}", patientUserId);

        medicationService.registerMedication(patientUserId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<List<MedicationResponseDTO>> getMyMedications(
            JwtAuthenticationToken token
    ) {
        log.info("[MEDICATION_CONTROLLER] Fetch my medications");

        return ResponseEntity.ok(medicationService.getMyMedications(token));
    }

    @GetMapping("/{medicationId}")
    @PreAuthorize("""
        hasAuthority('SCOPE_PACIENTE') 
        or hasAuthority('SCOPE_MEDICO') 
        or hasAuthority('SCOPE_CUIDADOR')
        or hasAuthority('SCOPE_ADMIN')
    """)
    public ResponseEntity<MedicationResponseDTO> getMedicationById(
            @PathVariable Long medicationId
    ) {
        log.info("[MEDICATION_CONTROLLER] Fetch medication id={}", medicationId);

        return ResponseEntity.ok(medicationService.getMedicationById(medicationId));
    }

    @PatchMapping("/{medicationId}/status")
    @PreAuthorize("""
        hasAuthority('SCOPE_PACIENTE') 
        or hasAuthority('SCOPE_MEDICO') 
        or hasAuthority('SCOPE_CUIDADOR')
    """)
    public ResponseEntity<Void> updateMedicationStatus(
            @PathVariable Long medicationId,
            @RequestParam MedicationStatus status
    ) {
        log.info(
                "[MEDICATION_CONTROLLER] Update medication status id={} to {}",
                medicationId, status
        );

        medicationService.updateMedicationStatus(medicationId, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{medicationId}")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> deleteMedication(
            @PathVariable Long medicationId
    ) {
        log.warn("[MEDICATION_CONTROLLER] Delete medication id={}", medicationId);

        medicationService.deleteMedication(medicationId);
        return ResponseEntity.noContent().build();
    }
}
