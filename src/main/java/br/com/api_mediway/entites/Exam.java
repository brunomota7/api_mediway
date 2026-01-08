package br.com.api_mediway.entites;

import br.com.api_mediway.enums.ConsultationAndExmStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_exams")
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

    @Enumerated(EnumType.STRING)
    private ConsultationAndExmStatus status;

}

