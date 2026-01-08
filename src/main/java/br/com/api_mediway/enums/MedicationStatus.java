package br.com.api_mediway.enums;

public enum MedicationStatus {
    ATIVO("Ativo"),
    SUSPENSO("Suspenso");

    private final String label;

    MedicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
