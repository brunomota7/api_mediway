package br.com.api_mediway.service;

import br.com.api_mediway.dto.request.exam.ExamRequestDTO;
import br.com.api_mediway.entites.Exam;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.exception.UserNotFoundException;
import br.com.api_mediway.factory.ExamFactory;
import br.com.api_mediway.repository.ExamRepository;
import br.com.api_mediway.repository.PatientInfosRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void scheduleExam(UUID patientId, ExamRequestDTO dto) {
        log.info("[EXAM] Scheduling new exam for patient Id={}", patientId);

        PatientInfos patient = patientInfosRepository.findByUserUserId(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));


        Exam exam = ExamFactory.registerExam(patient, dto);
    }

}
