package br.com.api_mediway.medication.enums;

public enum MedicationType {
    GENERICO("Genérico"),
    SIMILAR("Similar"),
    REFERENCIA("Referência"),
    FITOTERAPICO("Fitoterápico"),
    MANIPULADO("Manipulado"),
    OUTRO("Outro");

    private final String label;

    MedicationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
