package br.com.api_mediway.medicinebox.factory;


import br.com.api_mediway.medicinebox.dto.request.MedicineBoxRequestDTO;
import br.com.api_mediway.medicinebox.entity.MedicineBox;
import br.com.api_mediway.patient.entity.PatientInfos;
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
