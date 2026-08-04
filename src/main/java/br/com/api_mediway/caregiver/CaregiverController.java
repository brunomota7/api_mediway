package br.com.api_mediway.caregiver;

import br.com.api_mediway.caregiver.dto.request.CaregiverRequestDTO;
import br.com.api_mediway.caregiver.dto.response.CaregiverResponseDTO;
import br.com.api_mediway.caregiver.CaregiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/caregiver")
public class CaregiverController {

    private final CaregiverService caregiverService;

    public CaregiverController(CaregiverService caregiverService) {
        this.caregiverService = caregiverService;
    }

    // registrar cuidador
    @PostMapping("/register/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> registerCaregiver(
            @PathVariable UUID userId,
            @RequestBody CaregiverRequestDTO dto
    ) {
        log.info("[CAREGIVER] POST /caregiver/register/{}", userId);
        caregiverService.registerCaregiver(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // buscar cuidador por usuário
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<CaregiverResponseDTO> getCaregiverByUserId(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
                caregiverService.getCaregiverByUserId(userId)
        );
    }

    // vincular paciente
    @PutMapping("/{caregiverId}/assign-patient/{patientUserId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> assignPatient(
            @PathVariable Long caregiverId,
            @PathVariable UUID patientUserId
    ) {
        caregiverService.assignPatient(caregiverId, patientUserId);
        return ResponseEntity.noContent().build();
    }

    // remover paciente
    @PutMapping("/{caregiverId}/remove-patient/{patientUserId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> removePatient(
            @PathVariable Long caregiverId,
            @PathVariable UUID patientUserId
    ) {
        caregiverService.removePatient(caregiverId, patientUserId);
        return ResponseEntity.noContent().build();
    }

    // deletar cuidador (admin)
    @DeleteMapping("/{caregiverId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteCaregiver(
            @PathVariable Long caregiverId
    ) {
        caregiverService.deleteCaregiver(caregiverId);
        return ResponseEntity.noContent().build();
    }
}
