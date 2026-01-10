package br.com.api_mediway.factory;

import br.com.api_mediway.dto.request.vaccine.VaccineRequestDTO;
import br.com.api_mediway.entites.PatientInfos;
import br.com.api_mediway.entites.Vaccine;
import org.springframework.stereotype.Component;

@Component
public class VaccineFactory {

    public Vaccine create(
            VaccineRequestDTO dto,
            PatientInfos patient
    ) {
        Vaccine vaccine = new Vaccine();
        vaccine.setNome(dto.nome());
        vaccine.setTipoDose(dto.tipoDose());
        vaccine.setDataVacinou(dto.dataVacinou());
        vaccine.setLote(dto.lote());
        vaccine.setDataFabricacao(dto.dataFabricacao());
        vaccine.setProximaDose(dto.proximaDose());
        vaccine.setStatus(dto.status());
        vaccine.setPatientInfos(patient);
        return vaccine;
    }
}

