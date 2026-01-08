package br.com.api_mediway.enums;

public enum VaccineStatus {
    APLICADA("Aplicada"),
    AGENDADA("Agendada"),
    ATRASADA("Atrasada");

    private String label;

    VaccineStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
