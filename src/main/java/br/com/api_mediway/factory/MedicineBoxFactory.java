package br.com.api_mediway.factory;


import br.com.api_mediway.dto.request.medicineBox.MedicineBoxRequestDTO;
import br.com.api_mediway.entites.MedicineBox;
import br.com.api_mediway.entites.PatientInfos;
import org.springframework.stereotype.Component;

@Component
public class MedicineBoxFactory {

    public MedicineBox create(
            MedicineBoxRequestDTO dto,
            PatientInfos patient
    ) {
        MedicineBox box = new MedicineBox();
        box.setNome(dto.nome());
        box.setPatientInfos(patient);
        return box;
    }
}
