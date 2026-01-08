package br.com.api_mediway.entites;

import br.com.api_mediway.enums.VaccineDoseType;
import br.com.api_mediway.enums.VaccineStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_vaccines")
@Data
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private Long vaccineId;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "dose_type", nullable = false)
    private VaccineDoseType tipoDose;

    @Column(name = "vaccination_date")
    private LocalDateTime dataVacinou;

    @Column(nullable = false)
    private String lote;

    @Column(name = "manufacture_date")
    private LocalDate dataFabricacao;

    @Column(name = "next_dose_date")
    private LocalDate proximaDose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaccineStatus status = VaccineStatus.APLICADA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientInfos patientInfos;
}
