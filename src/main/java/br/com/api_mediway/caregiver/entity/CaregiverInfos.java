package br.com.api_mediway.caregiver.entity;

import br.com.api_mediway.patient.entity.PatientInfos;
import br.com.api_mediway.user.entity.User;

import br.com.api_mediway.common.enums.DoctorAndCaregiverAvailability;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_caregivers_info")
@Data
public class CaregiverInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caregiver_info_id")
    private Long caregiverInfoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String specialty;

    @Column(length = 500)
    private String experience;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorAndCaregiverAvailability availability;

    @OneToMany
    @JoinColumn(name = "caregiver_id")
    private List<PatientInfos> patients = new ArrayList<>();
}

