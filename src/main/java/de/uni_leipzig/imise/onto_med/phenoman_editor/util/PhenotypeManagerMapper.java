package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import care.smith.phep.phenoman.core.man.PhenotypeWriter;
import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import care.smith.phep.phenoman.core.exception.WrongPhenotypeTypeException;
import care.smith.phep.phenoman.core.man.PhenotypeManager;
import care.smith.phep.phenoman.core.model.code_system.Code;
import care.smith.phep.phenoman.core.model.phenotype.*;
import care.smith.phep.phenoman.core.model.phenotype.top_level.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This helper class provides some methods to write data into a PhenoMan ontology.
 */
public class PhenotypeManagerMapper {
	private PhenotypeManager model;

	public PhenotypeManagerMapper() { }

	@SuppressWarnings("unused")
	public PhenotypeManagerMapper(PhenotypeManager model) {
		this();
		this.model = model;
	}

	/**
	 * This method will add the provided {@see org.smith.phenoman.model.phenotype.top_level.Entity}
	 * to the {@link PhenotypeManager}.
	 * @param entity the {@link Entity}.
	 * @throws WrongPhenotypeTypeException If the specified {@link AbstractPhenotype} of the {@link Entity} does not exist.
	 */
	public void writeToOntology(
		@Nonnull Entity entity
	) throws WrongPhenotypeTypeException {
		PhenotypeWriter writer = model.getWriter();
		if (entity.isCategory()) {
			writer.addCategory(entity.asCategory());
		} else if (entity.isAbstractSinglePhenotype()) {
			writer.addAbstractSinglePhenotype(entity.asAbstractSinglePhenotype());
		} else if (entity.isAbstractCalculationPhenotype()) {
			writer.addAbstractCalculationPhenotype(entity.asAbstractCalculationPhenotype());
		} else if (entity.isAbstractBooleanPhenotype()) {
			writer.addAbstractBooleanPhenotype(entity.asAbstractBooleanPhenotype());
		} else if (entity.isRestrictedSinglePhenotype()) {
			writer.addRestrictedSinglePhenotype(entity.asRestrictedSinglePhenotype());
		} else if (entity.isRestrictedCalculationPhenotype()) {
			writer.addRestrictedCalculationPhenotype(entity.asRestrictedCalculationPhenotype());
		} else if (entity.isRestrictedBooleanPhenotype()) {
			writer.addRestrictedBooleanPhenotype(entity.asRestrictedBooleanPhenotype());
		}

		writer.write();
	}

	public @Nonnull Entity buildEntity(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		Entity entity;

		if (EntityType.CATEGORY.equals(data.getType())) {
			entity = buildCategory(data);
		} else if (data.getType() != null && data.getType().isAbstractPhenotype()) {
			entity = buildAbstractPhenotype(data);
		} else if (data.getType() != null && data.getType().isRestrictedPhenotype()) {
			entity = buildRestrictedPhenotype(data);
		} else {
			throw new UnsupportedDataTypeException("The provided phenotype data has an invalid datatype.");
		}

		entity.setOID(data.getOid());
		data.getTitles().forEach(t -> entity.addTitle(new Title(t.getString(), t.getLocale().toLanguageTag())));
		data.getSynonyms().forEach(s -> entity.addLabel(s.getString(), s.getLocale().toLanguageTag()));
		data.getDescriptions().forEach(d -> entity.addDescription(d.getString(), d.getLocale().toLanguageTag()));
		data.getCodes().forEach(c -> entity.addCode(new Code(c)));
		data.getRelations().forEach(entity::addRelatedConcept);

		return entity;
	}

	private @Nonnull Category buildCategory(@Nonnull PhenotypeBean data) {
		Category entity = new Category(data.getId(), data.getMainTitle());

		if (data.getSuperCategories().isEmpty()) {
			entity.setSuperCategories("Phenotype_Category");
		} else {
			entity.setSuperCategories(data.getSuperCategories().toArray(new String[0]));
		}

		return entity;
	}

	private @Nonnull AbstractPhenotype buildAbstractPhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		// TODO: restrictions are lost because they are tied to the AbstractPhenotype and they cannot be retrived from the PhenotypeManager!
		AbstractPhenotype entity;

		if (EntityType.ABSTRACT_SINGLE_PHENOTYPE.equals(data.getType())) {
			entity = buildAbstractSinglePhenotype(data);
		} else if (EntityType.ABSTRACT_CALCULATION_PHENOTYPE.equals(data.getType())) {
			entity = buildAbstractCalculationPhenotype(data);
		} else if (EntityType.ABSTRACT_BOOLEAN_PHENOTYPE.equals(data.getType())) {
			entity = buildAbstractBooleanPhenotype(data);
		} else {
			throw new UnsupportedDataTypeException("The provided phenotype data has an invalid datatype.");
		}
		entity.setCategories(data.getSuperCategories().toArray(new String[0]));

		return entity;
	}

	private @Nonnull RestrictedPhenotype buildRestrictedPhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		RestrictedPhenotype entity;

		if (EntityType.RESTRICTED_SINGLE_PHENOTYPE.equals(data.getType())) {
			entity = buildRestrictedSinglePhenotype(data);
		} else if (EntityType.RESTRICTED_CALCULATION_PHENOTYPE.equals(data.getType())) {
			entity = buildRestrictedCalculationPhenotype(data);
		} else if (EntityType.RESTRICTED_BOOLEAN_PHENOTYPE.equals(data.getType())) {
			entity = buildRestrictedBooleanPhenotype(data);
		} else {
			throw new UnsupportedDataTypeException(String.format(
				"The provided phenotype data has an invalid datatype %s.",
				data.getDatatype()
			));
		}
		entity.setScore(data.getScore());

		return entity;
	}

	private @Nonnull AbstractSinglePhenotype buildAbstractSinglePhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		AbstractSinglePhenotype entity;

		if (OWL2Datatype.XSD_DECIMAL.equals(data.getDatatype())) {
			entity = new AbstractSingleDecimalPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getSuperCategories().toArray(new String[0])
			);
		} else if (OWL2Datatype.XSD_DATE_TIME.equals(data.getDatatype())) {
			entity = new AbstractSingleDatePhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getSuperCategories().toArray(new String[0])
			);
		} else if (OWL2Datatype.XSD_BOOLEAN.equals(data.getDatatype())) {
			entity = new AbstractSingleBooleanPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getSuperCategories().toArray(new String[0])
			);
		} else {
			throw new UnsupportedDataTypeException(String.format(
				"The provided datatype %s for the %s is not supported.",
				data.getDatatype(), AbstractSinglePhenotype.class
			));
		}
		entity.setUnits(data.getUnits());
		/*
		 TODO:
		   * ResourceType resourceType
		   * TimePeriod validityPeriod
		   * EligibilityCriterion eligibilityCriterion
		   * String artDecorDatatype
		*/

		return entity;
	}

	private @Nonnull AbstractCalculationPhenotype buildAbstractCalculationPhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		AbstractCalculationPhenotype entity;

		if (OWL2Datatype.XSD_DECIMAL.equals(data.getDatatype())) {
			entity = new AbstractCalculationDecimalPhenotype(
				data.getId(),
				data.getMainTitle(),
				model.getFormula(data.getFormula()),
				data.getSuperCategories().toArray(new String[0])
			);
		} else if (OWL2Datatype.XSD_DATE_TIME.equals(data.getDatatype())) {
			entity = new AbstractCalculationDatePhenotype(
				data.getId(),
				data.getMainTitle(),
				model.getFormula(data.getFormula()),
				data.getSuperCategories().toArray(new String[0])
			);
		} else if (OWL2Datatype.XSD_BOOLEAN.equals(data.getDatatype())) {
			entity = new AbstractCalculationBooleanPhenotype(
				data.getId(),
				data.getMainTitle(),
				model.getFormula(data.getFormula()),
				data.getSuperCategories().toArray(new String[0])
			);
		} else {
			throw new UnsupportedDataTypeException(String.format(
				"The provided datatype %s for the %s is not supported.",
				data.getDatatype(), AbstractCalculationPhenotype.class
			));
		}
		entity.setMainResult(data.getMainResult());
		// TODO: BigDecimal value

		return entity;
	}

	private @Nonnull AbstractBooleanPhenotype buildAbstractBooleanPhenotype(@Nonnull PhenotypeBean data) {
		AbstractBooleanPhenotype entity = new AbstractBooleanPhenotype(
			data.getId(),
			data.getMainTitle(),
			data.getSuperCategories().toArray(new String[0])
		);
		entity.setMainResult(data.getMainResult());

		return entity;
	}

	private @Nonnull RestrictedSinglePhenotype buildRestrictedSinglePhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		AbstractSinglePhenotype parent = model.getReader().getAbstractSinglePhenotype(data.getSuperPhenotype());
		RestrictedSinglePhenotype entity;

		if (parent.hasBooleanDatatype()) {
			entity = parent.asAbstractSingleBooleanPhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asBooleanRange(),
				data.getNegated()
			);
		} else if (parent.hasDateDatatype()) {
			entity = parent.asAbstractSingleDatePhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asDateRange(),
				data.getNegated()
			);
		} else if (parent.hasDecimalDatatype()) {
			entity = parent.asAbstractSingleDecimalPhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asDecimalRange(),
				data.getNegated()
			);
		} else {
			throw new UnsupportedDataTypeException(String.format(
				"Super phenotype's datatype %s of the %s is not supported.",
				parent.getDatatype(), RestrictedCalculationPhenotype.class
			));
		}

		return entity;
	}

	private @Nonnull RestrictedCalculationPhenotype buildRestrictedCalculationPhenotype(@Nonnull PhenotypeBean data) throws UnsupportedDataTypeException {
		AbstractCalculationPhenotype parent = model.getReader().getAbstractCalculationPhenotype(data.getSuperPhenotype());
		RestrictedCalculationPhenotype entity;

		if (parent.hasBooleanDatatype()) {
			entity = parent.asAbstractCalculationBooleanPhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asBooleanRange(),
				data.getNegated()
			);
		} else if (parent.hasDateDatatype()) {
			entity = parent.asAbstractCalculationDatePhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asDateRange(),
				data.getNegated()
			);
		} else if (parent.hasDecimalDatatype()) {
			entity = parent.asAbstractCalculationDecimalPhenotype().createRestrictedPhenotype(
				data.getId(),
				data.getMainTitle(),
				data.getRestriction().asDecimalRange(),
				data.getNegated()
			);
		} else {
			throw new UnsupportedDataTypeException(String.format(
				"Super phenotype's datatype %s of the %s is not supported.",
				parent.getDatatype(), RestrictedCalculationPhenotype.class
			));
		}

		return entity;
	}

	private @Nonnull RestrictedBooleanPhenotype buildRestrictedBooleanPhenotype(@Nonnull PhenotypeBean data) {
		AbstractBooleanPhenotype parent = model.getReader().getAbstractBooleanPhenotype(data.getSuperPhenotype());

		RestrictedBooleanPhenotype entity = parent.createRestrictedPhenotype(
			data.getId(),
			data.getMainTitle(),
			model.getManchesterSyntaxExpression(data.getFormula())
		);
		entity.setMainResult(data.getMainResult());

		return entity;
	}

	public @Nonnull PhenotypeBean loadEntity(@Nonnull Entity entity) {
		PhenotypeBean bean;

		if (entity.isCategory()) {
			bean = loadCategoryAttributes(entity.asCategory());
		} else if (entity.isAbstractPhenotype()) {
			bean = loadAbstractAttributes(entity.asAbstractPhenotype());
		} else {
			bean = loadResctrictedAttributes(entity.asRestrictedPhenotype());
		}

		return bean
			.setType(EntityType.getEntityType(entity))
			.setId(entity.getName())
			.setOid(entity.getOID())
			.setMainTitle(entity.getMainTitleText())
			.setTitles(titleMapToLocalizedStringList(entity.getTitles()))
			.setSynonyms(stringMapToLocalizedStringList(entity.getLabels()))
			.setDescriptions(stringMapToLocalizedStringList(entity.getDescriptions()))
			.setCodes(entity.getCodes().stream().map(Code::getCodeUri).collect(Collectors.toList()))
			.setRelations(new ArrayList<>(entity.getRelatedConcepts()));
	}

	private @Nonnull PhenotypeBean loadCategoryAttributes(@Nonnull Category entity) {
		return new PhenotypeBean()
			.setSuperCategories(entity.getSuperCategoriesOrEmptyList());
	}

	private @Nonnull PhenotypeBean loadAbstractAttributes(@Nonnull AbstractPhenotype entity) {
		PhenotypeBean bean;

		if (entity.isAbstractSinglePhenotype()) {
			bean = loadAbstractSingleAttributes(entity.asAbstractSinglePhenotype());
		} else if (entity.isAbstractCalculationPhenotype()) {
			bean = loadAbstractCalculationAttributes(entity.asAbstractCalculationPhenotype());
		} else {
			bean = loadAbstractBooleanAttributes(entity.asAbstractBooleanPhenotype());
		}

		return bean.setSuperCategories(Arrays.asList(entity.getCategories()));
	}

	private @Nonnull PhenotypeBean loadAbstractSingleAttributes(@Nonnull AbstractSinglePhenotype entity) {
		return new PhenotypeBean()
			.setUnits(entity.asAbstractSinglePhenotype().getUnits())
			.setDatatype(entity.getDatatype());
		/*
		 TODO:
		   * Function function
		   * ResourceType resourceType
		   * TimePeriod validityPeriod
		   * EligibilityCriterion eligibilityCriterion
		   * String artDecorDatatype
		 */
	}

	private @Nonnull PhenotypeBean loadAbstractCalculationAttributes(@Nonnull AbstractCalculationPhenotype entity) {
		return new PhenotypeBean()
			.setUnits(entity.asAbstractCalculationPhenotype().getUnits())
			.setDatatype(entity.getDatatype())
			.setFormula(entity.getFormula())
			.setMainResult(entity.isMainResult());
		// TODO: BigDecimal value
	}

	private @Nonnull PhenotypeBean loadAbstractBooleanAttributes(@Nonnull AbstractBooleanPhenotype entity) {
		return new PhenotypeBean()
			.setMainResult(entity.isMainResult());
	}

	private @Nonnull PhenotypeBean loadResctrictedAttributes(@Nonnull RestrictedPhenotype entity) {
		PhenotypeBean bean;

		if (entity.isRestrictedSinglePhenotype()) {
			bean = loadRestrictedSingleAttributes(entity.asRestrictedSinglePhenotype());
		} else if (entity.isRestrictedCalculationPhenotype()) {
			bean = loadRestrictedCalculationAttributes(entity.asRestrictedCalculationPhenotype());
		} else {
			bean = loadRestrictedBooleanAttributes(entity.asRestrictedBooleanPhenotype());
		}

		return bean
			.setSuperPhenotype(entity.getAbstractPhenotypeName())
			.setScore(entity.getScore());
	}

	private @Nonnull PhenotypeBean loadRestrictedSingleAttributes(@Nonnull RestrictedSinglePhenotype entity) {
		return new PhenotypeBean()
			.setDatatype(entity.getDatatype())
			.setRestriction(entity.getPhenotypeRange())
			.setNegated(entity.isNegated());
		// TODO: some/all (for PhenotypeRange)
	}

	private @Nonnull PhenotypeBean loadRestrictedCalculationAttributes(@Nonnull RestrictedCalculationPhenotype entity) {
		return new PhenotypeBean()
			.setDatatype(entity.getDatatype())
			.setRestriction(entity.getPhenotypeRange())
			.setNegated(entity.isNegated());
	}

	private @Nonnull PhenotypeBean loadRestrictedBooleanAttributes(@Nonnull RestrictedBooleanPhenotype entity) {
		return new PhenotypeBean()
			.setFormula(entity.getFormula())
			.setMainResult(entity.isMainResult());
	}

	public void setModel(PhenotypeManager model) {
		this.model = model;
	}

	public PhenotypeManager getModel() {
		return model;
	}

	public boolean hasModel() {
		return model != null;
	}

	/**
	 * Transform the provided {@code Map<String, String>} into a list of {@link LocalizedString}s.
	 * @see PhenotypeManagerMapper#titleMapToLocalizedStringList(Map)
	 * @param map a {@link Map} containing strings in different languages
	 * @return a {@link List} of {@link LocalizedString}
	 */
	private List<LocalizedString> stringMapToLocalizedStringList(Map<String, Set<String>> map) {
		List<LocalizedString> stringList = new ArrayList<>();
		map.forEach((lang, list) ->
			list.forEach(text -> stringList.add(new LocalizedString(text, Locale.forLanguageTag(lang))))
		);
		return stringList;
	}

	/**
	 * Transform the provided {@code Map<String, Title>} into a list of {@link LocalizedString}.
	 * @param map a {@link Map} containing {@link Title}s in different languages
	 * @return a {@link List} of {@link LocalizedString}
	 */
	private List<LocalizedString> titleMapToLocalizedStringList(Map<String, Title> map) {
		List<LocalizedString> stringList = new ArrayList<>();
		map.forEach((lang, title) ->
			stringList.add(new LocalizedString(title.getTitleText(), Locale.forLanguageTag(lang)))
		);
		return stringList;
	}
}
