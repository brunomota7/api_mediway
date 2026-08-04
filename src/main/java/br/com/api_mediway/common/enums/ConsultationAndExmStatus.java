package br.com.api_mediway.common.enums;

public enum ConsultationAndExmStatus {
    MARCADO("Marcado"),
    REALIZADO("Realizado"),
    CANCELADO("Cancelado"),
    REMARCADO("Remarcado"),
    INDEFERIDO("Indefirido"),
    NAO_COMPARECEU("Não compareceu");

    private final String label;

    ConsultationAndExmStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

