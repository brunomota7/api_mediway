package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.exam.ExamRequestDTO;
import br.com.api_mediway.dto.request.exam.UpdateExamDTO;
import br.com.api_mediway.dto.response.Exam.ExamResponseDTO;
import br.com.api_mediway.entites.Exam;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.enums.ConsultationAndExmStatus;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.factory.ExamFactory;
import br.com.api_mediway.mapper.ExamMapper;
import br.com.api_mediway.repository.ExamRepository;
import br.com.api_mediway.repository.PatientInfosRepository;
import br.com.api_mediway.utils.UtilsService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final PatientInfosRepository patientInfosRepository;

    public ExamService(ExamRepository examRepository,
                       PatientInfosRepository patientInfosRepository) {
        this.examRepository = examRepository;
        this.patientInfosRepository = patientInfosRepository;
    }

    /**
     * Agenda um novo exame para o paciente
     */
    @Transactional
    public void scheduleExam(UUID patientId, ExamRequestDTO dto) {
        log.info("[EXAM] Scheduling new exam for patientId={}", patientId);

        PatientInfos patient = findPatientByUserId(patientId);

        Exam exam = ExamFactory.registerExam(patient, dto);

        examRepository.save(exam);

        log.info("[EXAM] Exam scheduled successfully for patientId={}", patientId);
    }

    /**
     * Retorna exames do usuário autenticado (PACIENTE)
     */
    @PreAuthorize("hasAuthority('SCOPE_PACIENTE')")
    public List<ExamResponseDTO> getUserExams(JwtAuthenticationToken token) {
        UUID userId = UtilsService.getUserIdFromToken(token);

        if (userId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID not found in token");

        log.info("[EXAM] Fetching exams for patient userId={}", userId);

        List<Exam> exams = examRepository.findByPatientInfosUserUserId(userId);

        log.info("[EXAM] Found {} exams for patient userId={}", exams.size(), userId);

        return exams.stream()
                .map(ExamMapper::toResponse)
                .toList();
    }

    /**
     * Retorna exames por data (ADMIN / CUIDADOR)
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public List<ExamResponseDTO> getExamsByDate(LocalDate date) {
        log.info("[EXAM] Fetching exams for date={}", date);

        return examRepository.findByExamDate(date)
                .stream()
                .map(ExamMapper::toResponse)
                .toList();
    }

    /**
     * Retorna exames por status
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public List<ExamResponseDTO> getExamsByStatus(ConsultationAndExmStatus status) {
        log.info("[EXAM] Fetching exams for status={}", status);

        return examRepository.findByStatus(status)
                .stream()
                .map(ExamMapper::toResponse)
                .toList();
    }

    /**
     * Busca exame por ID
     */
    public ExamResponseDTO getExamById(Long examId) {
        log.info("[EXAM] Fetching exam id={}", examId);

        Exam exam = findExamById(examId);
        return ExamMapper.toResponse(exam);
    }

    /**
     * Atualiza informações do exame (sem alterar status)
     */
    @Transactional
    public void updateExamInfo(Long examId, UpdateExamDTO dto) {
        log.info("[EXAM] Updating exam info id={}", examId);

        Exam exam = findExamById(examId);

        if (dto.typeExam() != null && !dto.typeExam().isBlank())
            exam.setTypeExam(dto.typeExam());

        if (dto.local() != null && !dto.local().isBlank())
            exam.setLocal(dto.local());

        if (dto.requeriments() != null && !dto.requeriments().isBlank())
            exam.setRequeriments(dto.requeriments());

        if (dto.examDate() != null)
            exam.setExamDate(dto.examDate());

        if (dto.examTime() != null)
            exam.setExamTime(dto.examTime());

        examRepository.save(exam);

        log.info("[EXAM] Exam updated successfully id={}", examId);
    }

    /**
     * Atualiza status do exame
     */
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_CUIDADOR')")
    public void updateExamStatus(Long examId, ConsultationAndExmStatus status) {
        log.info("[EXAM] Updating exam status id={}", examId);

        Exam exam = findExamById(examId);
        exam.setStatus(status);

        if (status == ConsultationAndExmStatus.CANCELADO) {
            exam.setCancellationDate(LocalDate.now());
        }

        examRepository.save(exam);

        log.info("[EXAM] Exam status updated successfully id={}", examId);
    }

    /**
     * Remove exame (uso administrativo)
     */
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteExam(Long examId) {
        log.warn("[EXAM] Deleting exam id={}", examId);

        Exam exam = findExamById(examId);
        examRepository.delete(exam);

        log.info("[EXAM] Exam deleted successfully id={}", examId);
    }

    /*
     * Métodos auxiliares
     */
    private Exam findExamById(Long examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));
    }

    private PatientInfos findPatientByUserId(UUID userId) {
        return patientInfosRepository.findByUserUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
    }
}

