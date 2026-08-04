package br.com.api_mediway.patient.enums;

public enum Gender {
    MASCULINO("Masculino"),
    FEMININO("Feminino"),
    NAO_INFORMADO("Não informado");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
