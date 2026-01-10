package br.com.api_mediway.controller;

import br.com.api_mediway.dto.response.medicineBox.MedicineBoxResponseDTO;
import br.com.api_mediway.service.MedicineBoxService;
import br.com.api_mediway.utils.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/medicine-box")
public class MedicineBoxController {

    private final MedicineBoxService medicineBoxService;

    public MedicineBoxController(MedicineBoxService medicineBoxService) {
        this.medicineBoxService = medicineBoxService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<MedicineBoxResponseDTO> getMyBox(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        return ResponseEntity.ok(medicineBoxService.getMyMedicineBox(userId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MEDICO') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<MedicineBoxResponseDTO> getBoxByUser(@PathVariable UUID patientId) {
        return ResponseEntity.ok(medicineBoxService.getMedicineBoxByUser(patientId));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> updateBoxName(
            JwtAuthenticationToken token,
            @RequestParam String nome
    ) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        medicineBoxService.updateMedicineBoxName(userId, nome);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/medication/{medicationId}")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId) {
        medicineBoxService.deleteMedication(medicationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/gaveta")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<Void> deleteGaveta(
            JwtAuthenticationToken token,
            @RequestParam String gaveta
    ) {
        UUID userId = UtilsService.getUserIdFromToken(token);
        medicineBoxService.deleteGaveta(userId, gaveta);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteBox(@PathVariable UUID userId) {
        medicineBoxService.deleteMedicineBox(userId);
        return ResponseEntity.noContent().build();
    }
}

