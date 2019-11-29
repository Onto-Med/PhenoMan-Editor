package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

public enum EntityType {
    CATEGORY("Category"),
    ABSTRACT_PHENOTYPE("Abstract Phenotype"),
    RESTRICTED_PHENOTYPE("Restricted Phenotype");

    private String title;

    EntityType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
