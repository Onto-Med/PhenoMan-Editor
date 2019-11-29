package de.uni_leipzig.imise.onto_med.phenoman_editor.bean;

import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.code_system.Code;
import org.smith.phenoman.model.phenotype.top_level.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class PhenotypeBean {
	private PhenotypeManager model;

	private String id;
	private String mainTitle;

	private List<Category> superCategories;
	private Phenotype superPhenotype;

	/*** localized: ***/
	private List<LocalizedString> titles = new ArrayList<>();
	private List<LocalizedString> synonyms = new ArrayList<>();
	private List<LocalizedString> descriptions = new ArrayList<>();

	private List<String> relations = new ArrayList<>();
	private List<String> codes = new ArrayList<>();
	private OWL2Datatype datatype;

	private OWL2Datatype formulaDatatype;
	private String formula;

	private List<DataRange> range = new ArrayList<>();
	private List<String> enumeration = new ArrayList<>();

	private String ucum;
	private BigDecimal score;

	public PhenotypeBean() {
	}

	public PhenotypeBean(Entity entity) {
		this();
		id        = entity.getName();
		mainTitle = entity.getMainTitleText();

		titles       = titleMapToLocalizedStringList(entity.getTitles());
		synonyms     = stringMapToLocalizedStringList(entity.getLabels());
		descriptions = stringMapToLocalizedStringList(entity.getDescriptions());

		relations = new ArrayList<>(entity.getRelatedConcepts());
		codes = entity.getCodes().stream().map(Code::getCodeUri).collect(Collectors.toList());

		if (entity.isAbstractSinglePhenotype()) datatype = entity.asAbstractSinglePhenotype().getDatatype();

		if (entity.isAbstractCalculationPhenotype()) { // is abstract calculation phenotype
			formulaDatatype = entity.asAbstractCalculationPhenotype().getDatatype();
			formula = entity.asAbstractCalculationPhenotype().getFormula();
		}

		if (entity.isAbstractSinglePhenotype()) { // is abstract single phenotype
			ucum = entity.asAbstractSinglePhenotype().getUnit();
		}

		if (!entity.isCategory() && !entity.isAbstractPhenotype()) { // is restricted phenotype
			score = ((RestrictedPhenotype) entity).getScore();
			// superPhenotype = model.getSuperPhenotype(entity);
		}
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

	public Phenotype getSuperPhenotype() {
		return superPhenotype;
	}

	public void setSuperPhenotype(Phenotype superPhenotype) {
		this.superPhenotype = superPhenotype;
	}

	public List<Category> getSuperCategories() {
		return superCategories;
	}

	public void setSuperCategories(List<Category> superCategories) {
		this.superCategories = superCategories;
	}

	public OWL2Datatype getFormulaDatatype() {
		return formulaDatatype;
	}

	public void setFormulaDatatype(OWL2Datatype formulaDatatype) {
		this.formulaDatatype = formulaDatatype;
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
}