package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.medication.MedicationRequestDTO;
import br.com.api_mediway.entites.Medication;
import br.com.api_mediway.entites.MedicineBox;
import br.com.api_mediway.entites.PatientInfos;
import org.springframework.stereotype.Component;

@Component
public class MedicationFactory {

    public Medication create(
            MedicationRequestDTO dto,
            PatientInfos patient,
            MedicineBox medicineBox
    ) {
        Medication medication = new Medication();
        medication.setNome(dto.nome());
        medication.setTipo(dto.tipo());
        medication.setNomeReferencia(dto.nomeReferencia());
        medication.setDescricao(dto.descricao());
        medication.setConcentracao(dto.concentracao());
        medication.setQuantidade(dto.quantidade());
        medication.setDias(dto.dias());
        medication.setHora(dto.hora());
        medication.setGaveta(dto.gaveta());
        medication.setEstoque(dto.estoque());
        medication.setStatus(dto.status());
        medication.setPatientInfos(patient);
        medication.setMedicineBox(medicineBox);

        return medication;
    }
}

