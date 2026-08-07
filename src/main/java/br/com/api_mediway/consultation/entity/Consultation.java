package br.com.api_mediway.consultation.entity;

import br.com.api_mediway.doctor.entity.DoctorInfos;
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
@Table(name = "tb_consultations")
@Getter
@Setter
@EqualsAndHashCode(of = "consultationId")
@ToString(exclude = {"patientInfos", "doctorInfos"})
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    private String description;
    private LocalDateTime requestDate = LocalDateTime.now();
    private String localConsultation;
    private String requeriments;

    @Column(name = "consultation_date")
    private LocalDate consultationDate;

    @Column(name = "consultation_time")
    private LocalTime consultationTime;

    @Enumerated(EnumType.STRING)
    private ConsultationAndExmStatus status = ConsultationAndExmStatus.MARCADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfos patientInfos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorInfos doctorInfos;

}

