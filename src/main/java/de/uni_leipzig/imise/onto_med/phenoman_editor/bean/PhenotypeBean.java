package de.uni_leipzig.imise.onto_med.phenoman_editor.bean;

import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smith.phenoman.exception.WrongPhenotypeTypeException;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.code_system.Code;
import org.smith.phenoman.model.phenotype.*;
import org.smith.phenoman.model.phenotype.top_level.*;

import java.math.BigDecimal;
import java.util.*;

import java.util.stream.Collectors;

public class PhenotypeBean {
	private PhenotypeManager model;

	private EntityType type;

	private String id;
	private String mainTitle;

	private List<String> superCategories = new ArrayList<>();
	private Phenotype superPhenotype;

	/*** localized: ***/
	private List<LocalizedString> titles = new ArrayList<>();
	private List<LocalizedString> synonyms = new ArrayList<>();
	private List<LocalizedString> descriptions = new ArrayList<>();

	private List<String> relations = new ArrayList<>();
	private List<String> codes = new ArrayList<>();
	private OWL2Datatype datatype;

	private String formula;

	private DataRange restriction;

	private String ucum;
	private BigDecimal score;

	public PhenotypeBean() {
	}

	public PhenotypeBean(Entity entity) {
		this();
		if (entity.isCategory()) {
			type = EntityType.CATEGORY;
		} else if (entity.isAbstractPhenotype()) {
			type = EntityType.ABSTRACT_PHENOTYPE;
		} else {
			type = EntityType.RESTRICTED_PHENOTYPE;
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
			// superPhenotype = model.getSuperPhenotype(entity);
            if (entity.isRangePhenotype())
                restriction = entity.asRangePhenotype().getPhenotypeRange();
		}
	}

	public void addToModel(PhenotypeManager model) throws WrongPhenotypeTypeException, IllegalArgumentException {
        if (type.equals(EntityType.CATEGORY)) {
			System.out.println("writing category to model");
        	Category category = new Category(id, mainTitle);

        	category.setSuperCategories(superCategories.toArray(String[]::new));
			descriptions.forEach(d -> category.addDescription(d.getString(), d.getLocale().toLanguageTag()));
			titles.forEach(t -> category.addTitle(new Title(t.getString(), t.getLocale().toLanguageTag())));
			synonyms.forEach(s -> category.addLabel(s.getString(), s.getLocale().toLanguageTag()));
			relations.forEach(category::addRelatedConcept);

			model.addCategory(category);
		} else if (type.equals(EntityType.ABSTRACT_PHENOTYPE)) {
			System.out.println("writing abstract phenotype to model");
            AbstractSinglePhenotype phenotype;
            switch (datatype) {
				case XSD_DECIMAL: phenotype = new AbstractSingleDecimalPhenotype(id, mainTitle); break;
                case XSD_STRING: phenotype = new AbstractSingleStringPhenotype(id, mainTitle); break;
                case XSD_DATE_TIME: phenotype = new AbstractSingleDatePhenotype(id, mainTitle); break;
                default: throw new IllegalArgumentException(datatype.getShortForm() + " is not supported");
            }

            phenotype.setCategories(superCategories.toArray(String[]::new));
            descriptions.forEach(d -> phenotype.addDescription(d.getString(), d.getLocale().toLanguageTag()));
            titles.forEach(t -> phenotype.addTitle(new Title(t.getString(), t.getLocale().toLanguageTag())));
            synonyms.forEach(s -> phenotype.addLabel(s.getString(), s.getLocale().toLanguageTag()));
            relations.forEach(phenotype::addRelatedConcept);
            codes.forEach(c -> phenotype.addCode(new Code(c)));
            phenotype.setUnit(ucum);

            model.addAbstractSinglePhenotype(phenotype);
        } else if (type.equals(EntityType.RESTRICTED_PHENOTYPE)) {
			System.out.println("writing restricted phenotype to model");
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

	public Phenotype getSuperPhenotype() {
		return superPhenotype;
	}

	public void setSuperPhenotype(Phenotype superPhenotype) {
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