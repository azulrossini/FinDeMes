package com.findemes.model;

public enum FrecuenciaEnum {

    DIARIO ("Diario"),
    SEMANAL("Semanal"),
    MENSUAL("Mensual"),
    BIMENSUAL("Bimensual"),
    ANUAL("Anual");

    private String displayName;

    FrecuenciaEnum(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
