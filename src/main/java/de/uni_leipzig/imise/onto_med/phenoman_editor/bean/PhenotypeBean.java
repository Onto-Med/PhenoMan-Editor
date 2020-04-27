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
	/**
	 * Describes, which type the phenotype is of. (@Code{EntityType})
	 * e.g. AbstractSinglePhenotype, RestrictedBooleanPhenotype, etc.
	 */
	private EntityType type;

	/**
	 * Unique identifier of the phenotype.
	 * Equivalent to {@code Phenotype.name} field.
	 */
	private String id;

	private String oid;
	private String mainTitle;

	private List<LocalizedString> titles       = new ArrayList<>();
	private List<LocalizedString> synonyms     = new ArrayList<>();
	private List<LocalizedString> descriptions = new ArrayList<>();

	private List<String> relations       = new ArrayList<>();
	private List<String> codes           = new ArrayList<>();
	private List<String> superCategories = new ArrayList<>();

	/**
	 * OWL2Datatype of the {@code AbstractSinglePhenotype} or of the formula of the {@code AbstractCalculationPhenotype}.
	 */
	private OWL2Datatype datatype;

	private List<String> units = new ArrayList<>();
	private BigDecimal   score;

	/**
	 * The {@code AbstractPhenotype} a {@code RestrictedPhenotype} is related to.
	 */
	private String superPhenotype;

	/**
	 * Specifies if the phenotype is negated.
	 */
	private Boolean negated;

	/**
	 * The formula of a {@code RestrictedBooleanPhenotype} or {@code AbstractCalculationPhenotype}.
	 */
	private String formula;

	private DataRange restriction;
	private Boolean mainResult;


	public PhenotypeBean() {
	}

	public PhenotypeBean(Entity entity) {
		this();
		type = EntityType.getEntityType(entity);

		if (entity.isCategory()) {
			loadCategoryAttributes(entity.asCategory());
		} else if (entity.isAbstractPhenotype()) {
			loadAbstractAttributes(entity.asAbstractPhenotype());
		} else if (entity.isRestrictedPhenotype()) {
			loadResctrictedAttributes(entity.asRestrictedPhenotype());
		}
	}

	public void addToModel(PhenotypeManager model) throws WrongPhenotypeTypeException, IllegalArgumentException {
		if (id == null || id.isEmpty()) id = UUID.randomUUID().toString();

		if (type.equals(EntityType.CATEGORY)) {
			Category category = new Category(id, mainTitle);
			category.setSuperCategories(superCategories.toArray(new String[0]));
			addMetadata(category);
			model.addCategory(category);
		} else if (type.equals(EntityType.ABSTRACT_SINGLE_PHENOTYPE)) {
			AbstractSinglePhenotype phenotype;
			switch (datatype) {
				case XSD_DECIMAL:
					phenotype = new AbstractSingleDecimalPhenotype(id, mainTitle);
					break;
				case XSD_STRING:
					phenotype = new AbstractSingleStringPhenotype(id, mainTitle);
					break;
				case XSD_DATE_TIME:
					phenotype = new AbstractSingleDatePhenotype(id, mainTitle);
					break;
				default:
					throw new IllegalArgumentException(datatype.getShortForm() + " is not supported.");
			}
			phenotype.setCategories(superCategories.toArray(new String[0]));
			addMetadata(phenotype);
			phenotype.setUnits(units);
			model.addAbstractSinglePhenotype(phenotype);
		} else if (type.equals(EntityType.ABSTRACT_BOOLEAN_PHENOTYPE)) {
			AbstractBooleanPhenotype phenotype = new AbstractBooleanPhenotype(id, mainTitle, superCategories.toArray(new String[0]));
			addMetadata(phenotype);
			model.addAbstractBooleanPhenotype(phenotype);
		} else if (type.equals(EntityType.ABSTRACT_CALCULATION_PHENOTYPE)) {
			// TODO: restrictions are lost because they are tied to the abstract phenotype
			AbstractCalculationPhenotype phenotype;
			switch (datatype) {
				case XSD_DECIMAL:
					phenotype = new AbstractCalculationDecimalPhenotype(id, mainTitle, model.getFormula(formula));
					break;
				case XSD_STRING:
					phenotype = new AbstractCalculationBooleanPhenotype(id, mainTitle, model.getFormula(formula));
					break;
				case XSD_DATE_TIME:
					phenotype = new AbstractCalculationDatePhenotype(id, mainTitle, model.getFormula(formula));
					break;
				default:
					throw new IllegalArgumentException(datatype.getShortForm() + " is not supported.");
			}
			phenotype.setCategories(superCategories.toArray(new String[0]));
			addMetadata(phenotype);
			model.addAbstractCalculationPhenotype(phenotype);
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

	private void loadCategoryAttributes(Category entity) {
		loadBasicMetadata(entity);
		superCategories = entity.getSuperCategoriesOrEmptyList();
	}

	private void loadAbstractAttributes(AbstractPhenotype entity) {
		loadBasicMetadata(entity);
		superCategories = Arrays.asList(entity.getCategories());

		if (entity.isAbstractSinglePhenotype()) {
			loadAbstractSingleAttributes(entity.asAbstractSinglePhenotype());
		} else if (entity.isAbstractCalculationPhenotype()) {
			loadAbstractCalculationAttributes(entity.asAbstractCalculationPhenotype());
		} else if (entity.isAbstractBooleanPhenotype()) {
			loadAbstractBooleanAttributes(entity.asAbstractBooleanPhenotype());
		}
	}

	private void loadAbstractSingleAttributes(AbstractSinglePhenotype entity) {
		units    = entity.asAbstractSinglePhenotype().getUnits();
		datatype = entity.getDatatype();

		/*
		 TODO:
		   * Function function
		   * ResourceType resourceType
		   * TimePeriod validityPeriod
		   * EligibilityCriterion eligibilityCriterion
		   * String artDecorDatatype
		 */
	}

	private void loadAbstractCalculationAttributes(AbstractCalculationPhenotype entity) {
		units      = entity.asAbstractCalculationPhenotype().getUnits();
		datatype   = entity.getDatatype();
		formula    = entity.getFormula();
		mainResult = entity.isMainResult();

		// TODO: BigDecimal value
	}

	private void loadAbstractBooleanAttributes(AbstractBooleanPhenotype entity) {
		mainResult = entity.isMainResult();
	}

	private void loadResctrictedAttributes(RestrictedPhenotype entity) {
		loadBasicMetadata(entity);

		superPhenotype = entity.getAbstractPhenotypeName();
		score          = entity.getScore();

		if (entity.isRestrictedSinglePhenotype()) {
			loadRestrictedSingleAttributes(entity.asRestrictedSinglePhenotype());
		} else if (entity.isRestrictedCalculationPhenotype()) {
			loadRestrictedCalculationAttributes(entity.asRestrictedCalculationPhenotype());
		} else if (entity.isRestrictedBooleanPhenotype()) {
			loadRestrictedBooleanAttributes(entity.asRestrictedBooleanPhenotype());
		}
	}

	private void loadRestrictedSingleAttributes(RestrictedSinglePhenotype entity) {
		datatype    = entity.getDatatype();
		restriction = entity.getPhenotypeRange();
		negated     = entity.isNegated();
		// TODO: some/all (for PhenotypeRange)
	}

	private void loadRestrictedCalculationAttributes(RestrictedCalculationPhenotype entity) {
		datatype    = entity.getDatatype();
		restriction = entity.getPhenotypeRange();
		negated     = entity.isNegated();
	}

	private void loadRestrictedBooleanAttributes(RestrictedBooleanPhenotype entity) {
		formula    = entity.getManchesterSyntaxExpression();
		mainResult = entity.isMainResult();
	}

	private void loadBasicMetadata(Entity entity) {
		id           = entity.getName();
		oid          = entity.getOID();
		mainTitle    = entity.getMainTitleText();
		titles       = titleMapToLocalizedStringList(entity.getTitles());
		synonyms     = stringMapToLocalizedStringList(entity.getLabels());
		descriptions = stringMapToLocalizedStringList(entity.getDescriptions());
		codes        = entity.getCodes().stream().map(Code::getCodeUri).collect(Collectors.toList());
		relations    = new ArrayList<>(entity.getRelatedConcepts());
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
		} else
			throw new IllegalArgumentException("RestrictedCalculationPhenotype could not be created because its super phenotype has an invalid datatype");
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