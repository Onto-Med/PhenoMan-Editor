package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

public enum EntityType {
    CATEGORY("Category"),
    ABSTRACT_SINGLE_PHENOTYPE("Abstract Phenotype"),
    ABSTRACT_CALCULATION_PHENOTYPE("Abstract Calculation Phenotype"),
    ABSTRACT_BOOLEAN_PHENOTYPE("Abstract Boolean Phenotype"),
    RESTRICTED_PHENOTYPE("Restricted Phenotype");

    private String title;

    EntityType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAbstractPhenotype() {
        return equals(ABSTRACT_SINGLE_PHENOTYPE) || equals(ABSTRACT_CALCULATION_PHENOTYPE) || equals(ABSTRACT_BOOLEAN_PHENOTYPE);
    }

    public boolean hasFormula() {
        return equals(ABSTRACT_CALCULATION_PHENOTYPE) || equals(ABSTRACT_BOOLEAN_PHENOTYPE);
    }
}
