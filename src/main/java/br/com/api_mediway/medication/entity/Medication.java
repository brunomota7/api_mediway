package br.com.api_mediway.medication.entity;

import br.com.api_mediway.medicinebox.entity.MedicineBox;
import br.com.api_mediway.patient.entity.PatientInfos;

import br.com.api_mediway.medication.enums.MedicationStatus;
import br.com.api_mediway.medication.enums.MedicationType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tb_medications")
@Data
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long medicationId;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicationType tipo;

    private String nomeReferencia;

    @Column(length = 500)
    private String descricao;

    private String concentracao;
    private String quantidade;


    private List<String> dias;
    private LocalTime hora;
    private String gaveta;
    private Integer estoque;

    @Enumerated(EnumType.STRING)
    private MedicationStatus status = MedicationStatus.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfos patientInfos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_box_id", nullable = false)
    private MedicineBox medicineBox;
}
