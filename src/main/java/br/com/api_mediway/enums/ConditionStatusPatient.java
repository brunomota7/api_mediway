package br.com.api_mediway.enums;

public enum ConditionStatusPatient {
    NESSECITA_DE_ATENCAO("Necessita de atenção"),
    EM_ACOMPANHAMENTO("Em acompanhamento"),
    ESTAVEL("Estável"),
    REABILITACAO("Reabilitação");

    private final String label;

    ConditionStatusPatient(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
