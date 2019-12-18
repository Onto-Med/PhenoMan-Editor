package de.uni_leipzig.imise.onto_med.phenoman_editor.bean;

import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.smith.phenoman.exception.WrongPhenotypeTypeException;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.code_system.Code;
import org.smith.phenoman.model.phenotype.*;
import org.smith.phenoman.model.phenotype.top_level.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PhenotypeBean {
	private EntityType type;

	private String id;
	private String mainTitle;

	private List<String> superCategories = new ArrayList<>();
	private String superPhenotype;

	/*** localized: ***/
	private List<LocalizedString> titles = new ArrayList<>();
	private List<LocalizedString> synonyms = new ArrayList<>();
	private List<LocalizedString> descriptions = new ArrayList<>();

	private List<String> relations = new ArrayList<>();
	private List<String> codes = new ArrayList<>();
	private OWL2Datatype datatype;

	private String formula;

	private DataRange restriction;
	private Boolean negated;

	private String ucum;
	private BigDecimal score;

	public PhenotypeBean() {
	}

	public PhenotypeBean(Entity entity) {
		this();
		type = EntityType.getEntityType(entity);
		if (entity.isCategory()) {
			superCategories = entity.asCategory().getSuperCategoriesOrEmptyList();
		} else if (entity.isAbstractPhenotype()) {
			superCategories = Arrays.asList(entity.asAbstractPhenotype().getCategories());
		} else {
			superPhenotype = ((RestrictedPhenotype) entity).getAbstractPhenotypeName();
		}
		id        = entity.getName();
		mainTitle = entity.getMainTitleText();

		titles       = titleMapToLocalizedStringList(entity.getTitles());
		synonyms     = stringMapToLocalizedStringList(entity.getLabels());
		descriptions = stringMapToLocalizedStringList(entity.getDescriptions());

		relations = new ArrayList<>(entity.getRelatedConcepts());
		codes = entity.getCodes().stream().map(Code::getCodeUri).collect(Collectors.toList());

		if (entity.isAbstractSinglePhenotype()) datatype = entity.asAbstractSinglePhenotype().getDatatype();

		if (entity.isAbstractCalculationPhenotype()) { // is abstract calculation phenotype
			datatype = entity.asAbstractCalculationPhenotype().getDatatype();
			formula = entity.asAbstractCalculationPhenotype().getFormula();
		}

		if (entity.isAbstractSinglePhenotype()) { // is abstract single phenotype
			ucum = entity.asAbstractSinglePhenotype().getUnit();
		}

		if (!entity.isCategory() && !entity.isAbstractPhenotype()) { // is restricted phenotype
			score = ((RestrictedPhenotype) entity).getScore();
            if (entity.isRangePhenotype())
                restriction = entity.asRangePhenotype().getPhenotypeRange();
            if (entity.isRestrictedBooleanPhenotype()) {
            	formula = entity.asRestrictedBooleanPhenotype().getFormula();
			}
		}
	}

	public void addToModel(PhenotypeManager model) throws WrongPhenotypeTypeException, IllegalArgumentException {
		if (id == null || id.isBlank())	id = UUID.randomUUID().toString();

        if (type.equals(EntityType.CATEGORY)) {
        	Category category = new Category(id, mainTitle);
        	category.setSuperCategories(superCategories.toArray(String[]::new));
        	addMetadata(category);
			model.addCategory(category);
		} else if (type.isAbstractPhenotype()) {
            AbstractSinglePhenotype phenotype;
            switch (datatype) {
				case XSD_DECIMAL: phenotype = new AbstractSingleDecimalPhenotype(id, mainTitle); break;
                case XSD_STRING: phenotype = new AbstractSingleStringPhenotype(id, mainTitle); break;
                case XSD_DATE_TIME: phenotype = new AbstractSingleDatePhenotype(id, mainTitle); break;
                default: throw new IllegalArgumentException(datatype.getShortForm() + " is not supported.");
            }
            phenotype.setCategories(superCategories.toArray(String[]::new));
            addMetadata(phenotype);
            phenotype.setUnit(ucum);
            model.addAbstractSinglePhenotype(phenotype);
        } else if (type.isRestrictedPhenotype()) {
        	Phenotype abstractPhenotype = model.getPhenotype(superPhenotype);
			if (abstractPhenotype == null) {
				throw new IllegalArgumentException("super phenotype is missing");
			} else if (abstractPhenotype.isAbstractBooleanPhenotype()) {
				RestrictedBooleanPhenotype phenotype = abstractPhenotype.asAbstractBooleanPhenotype().createRestrictedPhenotype(id, mainTitle, model.getManchesterSyntaxExpression(formula));
				addMetadata(phenotype);
				phenotype.setScore(score);
				model.addRestrictedBooleanPhenotype(phenotype);
			} else if (abstractPhenotype.isAbstractCalculationPhenotype()) {
				model.addRestrictedCalculationPhenotype(createRestrictedCalculationPhenotype(abstractPhenotype.asAbstractCalculationPhenotype()));
			} else if (abstractPhenotype.isAbstractSinglePhenotype()) {
				model.addRestrictedSinglePhenotype(createRestrictedSinglePhenotype(abstractPhenotype.asAbstractSinglePhenotype()));
			} else {
				throw new IllegalArgumentException("Could not determine datatype of super phenotype.");
			}
		}
        model.write();
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public List<LocalizedString> getTitles() {
		return titles;
	}

	public void setTitles(List<LocalizedString> titles) {
		this.titles = titles;
	}

	public List<LocalizedString> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<LocalizedString> synonyms) {
		this.synonyms = synonyms;
	}

	public List<LocalizedString> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<LocalizedString> descriptions) {
		this.descriptions = descriptions;
	}

	public List<String> getRelations() {
		return relations;
	}

	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public OWL2Datatype getDatatype() {
		return datatype;
	}

	public void setDatatype(OWL2Datatype datatype) {
		this.datatype = datatype;
	}

	public String getUcum() {
		return ucum;
	}

	public void setUcum(String ucum) {
		this.ucum = ucum;
	}

	public String getSuperPhenotype() {
		return superPhenotype;
	}

	public void setSuperPhenotype(String superPhenotype) {
		this.superPhenotype = superPhenotype;
	}

	public List<String> getSuperCategories() {
		return superCategories;
	}

	public void setSuperCategories(List<String> superCategories) {
		this.superCategories = superCategories;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

    public DataRange getRestriction() {
        return restriction;
    }

    public void setRestriction(DataRange restriction) {
        this.restriction = restriction;
    }

	public Boolean getNegated() {
		return negated;
	}

	public void setNegated(Boolean negated) {
		this.negated = negated;
	}

	private List<LocalizedString> stringMapToLocalizedStringList(Map<String, Set<String>> map) {
		List<LocalizedString> stringList = new ArrayList<>();
		map.forEach((lang, list) ->
				list.forEach(text -> stringList.add(new LocalizedString(text, Locale.forLanguageTag(lang))))
		);
		return stringList;
	}

	private List<LocalizedString> titleMapToLocalizedStringList(Map<String, Title> map) {
		List<LocalizedString> stringList = new ArrayList<>();
		map.forEach((lang, title) ->
				stringList.add(new LocalizedString(title.getTitleText(), Locale.forLanguageTag(lang)))
		);
		return stringList;
	}

	private RestrictedSinglePhenotype createRestrictedSinglePhenotype(AbstractSinglePhenotype abstractPhenotype) {
		RestrictedSinglePhenotype phenotype;
		if (abstractPhenotype.hasBooleanDatatype()) {
			phenotype = abstractPhenotype.asAbstractSingleBooleanPhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asBooleanRange(), negated);
		} else if (abstractPhenotype.hasDecimalDatatype()) {
			phenotype = abstractPhenotype.asAbstractSingleDecimalPhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asDecimalRange(), negated);
		} else if (abstractPhenotype.hasDateDatatype()) {
			phenotype = abstractPhenotype.asAbstractSingleDatePhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asDateRange(), negated);
		} else if (abstractPhenotype.hasStringDatatype()) {
			phenotype = abstractPhenotype.asAbstractSingleStringPhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asStringRange(), negated);
		} else {
			throw new IllegalArgumentException("Super phenotype has a not supported data type. Maybe the ontology is inconsistent or was created with an old version of PhenoMan?");
		}
		addMetadata(phenotype);
		phenotype.setScore(score);
		return phenotype;
	}

	private RestrictedCalculationPhenotype createRestrictedCalculationPhenotype(AbstractCalculationPhenotype abstractPhenotype) {
		RestrictedCalculationPhenotype phenotype;
		if (abstractPhenotype.hasBooleanDatatype()) {
			phenotype = abstractPhenotype.asAbstractCalculationBooleanPhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asBooleanRange(), negated);
		} else if (abstractPhenotype.hasDateDatatype()) {
			phenotype = abstractPhenotype.asAbstractCalculationDatePhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asDateRange(), negated);
		} else if (abstractPhenotype.hasDecimalDatatype()) {
			phenotype = abstractPhenotype.asAbstractCalculationDecimalPhenotype().createRestrictedPhenotype(id, mainTitle, restriction.asDecimalRange(), negated);
		} else throw new IllegalArgumentException("RestrictedCalculationPhenotype could not be created because its super phenotype has an invalid datatype");
		addMetadata(phenotype);
		phenotype.setScore(score);
		return phenotype;
	}

	private void addMetadata(Entity entity) {
		titles.forEach(t -> entity.addTitle(new Title(t.getString(), t.getLocale().toLanguageTag())));
		synonyms.forEach(s -> entity.addLabel(s.getString(), s.getLocale().toLanguageTag()));
		descriptions.forEach(d -> entity.addDescription(d.getString(), d.getLocale().toLanguageTag()));
		relations.forEach(entity::addRelatedConcept);
		codes.forEach(c -> entity.addCode(new Code(c)));
	}
}