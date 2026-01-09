package br.com.api_mediway.entites;

import br.com.api_mediway.enums.ConsultationAndExmStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tb_exams")
@Data
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

