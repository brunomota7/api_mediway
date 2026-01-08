package br.com.api_mediway.enums;

public enum DoctorAndCaregiverAvailability {
    INTEGRAL("Integral"),
    PARCIAL_MANHA("Parcial - Matutino"),
    PARCIAL_TARDE("Parcial - Vespertino"),
    PLANTOES("Plantões");

    private final String label;

    DoctorAndCaregiverAvailability(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
