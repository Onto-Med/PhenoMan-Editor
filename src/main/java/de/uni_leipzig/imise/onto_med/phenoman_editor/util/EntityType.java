package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import care.smith.phep.phenoman.core.model.phenotype.top_level.Entity;

public enum EntityType {
    CATEGORY("Category"),
    ABSTRACT_SINGLE_PHENOTYPE("Abstract Single Phenotype"),
    ABSTRACT_CALCULATION_PHENOTYPE("Abstract Calculation Phenotype"),
    ABSTRACT_BOOLEAN_PHENOTYPE("Abstract Boolean Phenotype"),
    RESTRICTED_SINGLE_PHENOTYPE("Restricted Single Phenotype"),
    RESTRICTED_CALCULATION_PHENOTYPE("Restricted Calculation Phenotype"),
    RESTRICTED_BOOLEAN_PHENOTYPE("Restricted Boolean Phenotype");

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

    public boolean isRestrictedPhenotype() {
        return equals(RESTRICTED_SINGLE_PHENOTYPE) || equals(RESTRICTED_CALCULATION_PHENOTYPE) || equals(RESTRICTED_BOOLEAN_PHENOTYPE);
    }

    public boolean hasFormula() {
        return equals(ABSTRACT_CALCULATION_PHENOTYPE) || equals(RESTRICTED_BOOLEAN_PHENOTYPE);
    }

    public boolean hasDatatype() {
        return isAbstractPhenotype() && !equals(EntityType.ABSTRACT_BOOLEAN_PHENOTYPE);
    }

    public boolean hasRestriction() {
        return isRestrictedPhenotype() && !equals(EntityType.RESTRICTED_BOOLEAN_PHENOTYPE);
    }

    public static EntityType getEntityType(Entity entity) {
        if (entity.isCategory()) return EntityType.CATEGORY;

        if (entity.isAbstractPhenotype()) {
            if (entity.isAbstractSinglePhenotype()) {
                return EntityType.ABSTRACT_SINGLE_PHENOTYPE;
            } else if (entity.isAbstractCalculationPhenotype()) {
                return EntityType.ABSTRACT_CALCULATION_PHENOTYPE;
            }
            return EntityType.ABSTRACT_BOOLEAN_PHENOTYPE;
        }

        if (entity.isRestrictedSinglePhenotype()) {
            return EntityType.RESTRICTED_SINGLE_PHENOTYPE;
        } else if (entity.isRestrictedCalculationPhenotype()) {
            return EntityType.RESTRICTED_CALCULATION_PHENOTYPE;
        }
        return EntityType.RESTRICTED_BOOLEAN_PHENOTYPE;
    }
}
