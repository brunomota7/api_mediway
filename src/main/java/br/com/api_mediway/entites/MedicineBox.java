package br.com.api_mediway.entites;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_medicine_box")
@Data
public class MedicineBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_box_id")
    private Long medicineBoxId;

    private String nome;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private PatientInfos patientInfos;

    @OneToMany(
            mappedBy = "medicineBox",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Medication> medications = new ArrayList<>();
}
