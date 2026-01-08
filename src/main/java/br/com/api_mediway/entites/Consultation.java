package br.com.api_mediway.entites;

import br.com.api_mediway.enums.ConsultationAndExmStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_consultations")
@Data
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    private String description;
    private LocalDateTime requestDate = LocalDateTime.now();
    private String localConsultation;
    private String requeriments;

    @Enumerated(EnumType.STRING)
    private ConsultationAndExmStatus status = ConsultationAndExmStatus.MARCADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfos patientInfos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorInfos doctorInfos;

}

