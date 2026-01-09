package br.com.api_mediway.controller;

import br.com.api_mediway.dto.request.consultation.ConsultationRequestDTO;
import br.com.api_mediway.dto.request.consultation.UpdateConsultationDTO;
import br.com.api_mediway.dto.response.consultation.ConsultationResponseDTO;
import br.com.api_mediway.enums.ConsultationAndExmStatus;
import br.com.api_mediway.service.ConsultationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/consultation")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    // agendar consulta
    @PostMapping("/schedule/{patientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<Void> scheduleConsultation(
            @PathVariable UUID patientId,
            @RequestBody ConsultationRequestDTO dto
    ) {
        log.info("[CONSULTATION] POST /consultation/schedule/{} - Scheduling new consultation", patientId);
        consultationService.scheduleConsultation(patientId, dto);
        log.info("[CONSULTATION] Consultation scheduled successfully for patientId={}", patientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // consultas do paciente/médico logado
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<List<ConsultationResponseDTO>> getMyConsultations(JwtAuthenticationToken token) {
        log.info("[CONSULTATION] GET /consultation/me - Fetching consultations for logged user");
        List<ConsultationResponseDTO> consultations = consultationService.getUserConsultations(token);
        log.info("[CONSULTATION] Found {} consultations for logged user", consultations.size());
        return ResponseEntity.ok(consultations);
    }

    // consultas por ID do usuário
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<List<ConsultationResponseDTO>> getConsultationsByUserId(@PathVariable UUID userId) {
        log.info("[CONSULTATION] GET /consultation/user/{} - Fetching consultations by userId", userId);
        List<ConsultationResponseDTO> consultations = consultationService.getConsultationsByUserId(userId);
        log.info("[CONSULTATION] Found {} consultations for userId={}", consultations.size(), userId);
        return ResponseEntity.ok(consultations);
    }

    // consulta específica por ID
    @GetMapping("/{consultationId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_PACIENTE') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<ConsultationResponseDTO> getConsultationById(@PathVariable Long consultationId) {
        log.info("[CONSULTATION] GET /consultation/{} - Fetching consultation by ID", consultationId);
        ConsultationResponseDTO consultation = consultationService.getConsultationById(consultationId);
        log.info("[CONSULTATION] Consultation fetched successfully id={}", consultationId);
        return ResponseEntity.ok(consultation);
    }

    // consultas por data específica
    @GetMapping("/by-date")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<List<ConsultationResponseDTO>> getConsultationsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("[CONSULTATION] GET /consultation/by-date?date={} - Fetching consultations by date", date);
        List<ConsultationResponseDTO> consultations = consultationService.getConsultationsByDate(date);
        log.info("[CONSULTATION] Found {} consultations for date={}", consultations.size(), date);
        return ResponseEntity.ok(consultations);
    }

    // buscando consultas por status
    @GetMapping("/by-status")
    public ResponseEntity<List<ConsultationResponseDTO>> getConsultationByStatus(
            @RequestParam("status") ConsultationAndExmStatus status
    ) {
        log.info("[CONSULTATION] GET /consultation/by-status?status={} - Fetching consultations by status", status);
        List<ConsultationResponseDTO> consultations = consultationService.getConsultationByStatus(status);
        log.info("[CONSULTATION]  Found {} consultations for status={}", consultations.size(), status);
        return ResponseEntity.ok(consultations);
    }

    // atualizar informações de uma consulta
    @PutMapping("/{consultationId}/update")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> updateConsultationInfo(
            @PathVariable Long consultationId,
            @RequestBody UpdateConsultationDTO dto
    ) {
        log.info("[CONSULTATION] PUT /consultation/{}/update - Updating consultation info", consultationId);
        consultationService.updateConsultationsInfo(consultationId, dto);
        log.info("[CONSULTATION] Consultation info updated successfully id={}", consultationId);
        return ResponseEntity.noContent().build();
    }

    // atualizar status de uma consulta
    @PutMapping("/{consultationId}/status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> updateConsultationStatus(
            @PathVariable Long consultationId,
            @RequestParam ConsultationAndExmStatus status
    ) {
        log.info("[CONSULTATION] PUT /consultation/{}/status - Changing status to {}", consultationId, status);
        consultationService.updateConsultationStatus(consultationId, status);
        log.info("[CONSULTATION] Consultation status updated successfully id={}, newStatus={}", consultationId, status);
        return ResponseEntity.noContent().build();
    }

    // deletar consulta
    @DeleteMapping("/{consultationId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR') or hasAuthority('SCOPE_MEDICO')")
    public ResponseEntity<Void> deleteConsultation(@PathVariable Long consultationId) {
        log.warn("[CONSULTATION] DELETE /consultation/{} - Deleting consultation", consultationId);
        consultationService.deleteConsultation(consultationId);
        log.info("[CONSULTATION] Consultation deleted successfully id={}", consultationId);
        return ResponseEntity.noContent().build();
    }
}
