package br.com.api_mediway.exam;

import br.com.api_mediway.exam.dto.request.ExamRequestDTO;
import br.com.api_mediway.exam.dto.request.UpdateExamDTO;
import br.com.api_mediway.exam.dto.response.ExamResponseDTO;
import br.com.api_mediway.common.enums.ConsultationAndExmStatus;
import br.com.api_mediway.exam.ExamService;
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
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    // agendar exame
    @PostMapping("/schedule/{patientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<Void> scheduleExam(
            @PathVariable UUID patientId,
            @RequestBody ExamRequestDTO dto
    ) {
        log.info("[EXAM] POST /exam/schedule/{} - Scheduling new exam", patientId);
        examService.scheduleExam(patientId, dto);
        log.info("[EXAM] Exam scheduled successfully for patientId={}", patientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // exames do paciente logado
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public ResponseEntity<List<ExamResponseDTO>> getMyExams(JwtAuthenticationToken token) {
        log.info("[EXAM] GET /exam/me - Fetching exams for logged patient");
        List<ExamResponseDTO> exams = examService.getUserExams(token);
        log.info("[EXAM] Found {} exams for logged patient", exams.size());
        return ResponseEntity.ok(exams);
    }

    // exame específico por ID
    @GetMapping("/{examId}")
    @PreAuthorize(
            "hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_PACIENTE') or hasAuthority('SCOPE_CUIDADOR')"
    )
    public ResponseEntity<ExamResponseDTO> getExamById(@PathVariable Long examId) {
        log.info("[EXAM] GET /exam/{} - Fetching exam by ID", examId);
        ExamResponseDTO exam = examService.getExamById(examId);
        log.info("[EXAM] Exam fetched successfully id={}", examId);
        return ResponseEntity.ok(exam);
    }

    // exames por data
    @GetMapping("/by-date")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<List<ExamResponseDTO>> getExamsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("[EXAM] GET /exam/by-date?date={} - Fetching exams by date", date);
        List<ExamResponseDTO> exams = examService.getExamsByDate(date);
        log.info("[EXAM] Found {} exams for date={}", exams.size(), date);
        return ResponseEntity.ok(exams);
    }

    // exames por status
    @GetMapping("/by-status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<List<ExamResponseDTO>> getExamsByStatus(
            @RequestParam("status") ConsultationAndExmStatus status
    ) {
        log.info("[EXAM] GET /exam/by-status?status={} - Fetching exams by status", status);
        List<ExamResponseDTO> exams = examService.getExamsByStatus(status);
        log.info("[EXAM] Found {} exams for status={}", exams.size(), status);
        return ResponseEntity.ok(exams);
    }

    // atualizar informações do exame
    @PutMapping("/{examId}/update")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<Void> updateExamInfo(
            @PathVariable Long examId,
            @RequestBody UpdateExamDTO dto
    ) {
        log.info("[EXAM] PUT /exam/{}/update - Updating exam info", examId);
        examService.updateExamInfo(examId, dto);
        log.info("[EXAM] Exam info updated successfully id={}", examId);
        return ResponseEntity.noContent().build();
    }

    // atualizar status do exame
    @PutMapping("/{examId}/status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public ResponseEntity<Void> updateExamStatus(
            @PathVariable Long examId,
            @RequestParam ConsultationAndExmStatus status
    ) {
        log.info("[EXAM] PUT /exam/{}/status - Changing status to {}", examId, status);
        examService.updateExamStatus(examId, status);
        log.info("[EXAM] Exam status updated successfully id={}, newStatus={}", examId, status);
        return ResponseEntity.noContent().build();
    }

    // deletar exame
    @DeleteMapping("/{examId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        log.warn("[EXAM] DELETE /exam/{} - Deleting exam", examId);
        examService.deleteExam(examId);
        log.info("[EXAM] Exam deleted successfully id={}", examId);
        return ResponseEntity.noContent().build();
    }
}

