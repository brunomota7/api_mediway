package br.com.api_mediway.doctor.entity;

import br.com.api_mediway.consultation.entity.Consultation;
import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.user.entity.User;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_doctors")
@Data
public class DoctorInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_info_id")
    private Long doctorInfosId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(unique = true)
    private String crm;
    private String specialty;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    private DoctorAndCaregiverAvailability availability;

    @OneToMany(mappedBy = "doctorInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientInfos> patients = new ArrayList<>();


}
