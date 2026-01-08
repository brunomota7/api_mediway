package br.com.api_mediway.entites;

import br.com.api_mediway.enums.ConditionStatusPatient;
import br.com.api_mediway.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_patients_info")
@Data
public class PatientInfos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_info_id")
    private Long patientInfoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private LocalDate dateOfBirth;
    private Integer age;
    private String conditionPatient;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_patient")
    private ConditionStatusPatient statusPatient;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_patient")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorInfos doctor;

    @OneToMany(mappedBy = "patientInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();

    @OneToMany(mappedBy = "patientInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    @OneToOne(mappedBy = "patientInfos", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private MedicineBox medicineBox;

    @OneToMany(mappedBy = "patientInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications = new ArrayList<>();

    @OneToMany(mappedBy = "patientInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaccine> vaccines = new ArrayList<>();

}

