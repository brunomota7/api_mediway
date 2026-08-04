package br.com.api_mediway.vaccine.enums;

public enum VaccineDoseType {

    UNICA_DOSE("Única Dose"),
    PRIMEIRA_DOSE("Primeira Dose"),
    SEGUNDA_DOSE("Segunda Dose"),
    REFORCO("Reforço");

    private final String label;

    VaccineDoseType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
