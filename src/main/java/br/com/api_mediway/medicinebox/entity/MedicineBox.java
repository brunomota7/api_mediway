package br.com.api_mediway.medicinebox.entity;

import br.com.api_mediway.medication.entity.Medication;
import br.com.api_mediway.patient.entity.PatientInfos;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_medicine_box")
@Getter
@Setter
@EqualsAndHashCode(of = "medicineBoxId")
@ToString(exclude = {"patientInfos", "medications"})
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
