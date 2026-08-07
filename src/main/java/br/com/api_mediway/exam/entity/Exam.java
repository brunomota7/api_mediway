package br.com.api_mediway.exam.entity;

import br.com.api_mediway.patient.entity.PatientInfos;

import br.com.api_mediway.common.enums.ConsultationAndExmStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tb_exams")
@Getter
@Setter
@EqualsAndHashCode(of = "examId")
@ToString(exclude = "patientInfos")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfos patientInfos;

    private String typeExam;
    private LocalDateTime requestDate = LocalDateTime.now();
    private String local;
    private String requeriments;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(name = "exam_time")
    private LocalTime examTime;

    @Enumerated(EnumType.STRING)
    private ConsultationAndExmStatus status;
    private LocalDate cancellationDate;

}

