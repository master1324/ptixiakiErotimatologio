package com.p16021.ptixiaki.erotimatologio.models.enums;

public enum IdentifierType {

    TEACHER("Καθηγητής"),
    SUBJECT("Μάθημα"),
    DEPARTMENT("Τμήμα"),
    EXAMINO("Αρ. Εξαμήνου"),
    SEMESTER("Εξάμηνο"),
    YEAR("Έτος");

    public final String label;

    private IdentifierType(String label){
        this.label = label;
    }
}
