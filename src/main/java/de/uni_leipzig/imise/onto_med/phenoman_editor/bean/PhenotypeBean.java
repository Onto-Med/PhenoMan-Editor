package de.uni_leipzig.imise.onto_med.phenoman_editor.bean;

import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.smith.phenoman.model.phenotype.top_level.Category;
import org.smith.phenoman.model.phenotype.top_level.Entity;
import org.smith.phenoman.model.phenotype.top_level.Phenotype;

import java.util.ArrayList;
import java.util.List;

public class PhenotypeBean {
	private Entity entity;
	private String id;
	private String mainTitle;

	private List<Category> superCategories;
	private Phenotype superPhenotype;

	/*** localized: ***/
	private List<LocalizedString> labels = new ArrayList<>();
	private List<LocalizedString> titles = new ArrayList<>();
	private List<LocalizedString> synonyms = new ArrayList<>();
	private List<LocalizedString> descriptions = new ArrayList<>();

	private List<String> relations = new ArrayList<>();
	private List<String> codes = new ArrayList<>();
	private OWL2Datatype datatype;

	private List<DataRange> range = new ArrayList<>();
	private List<String> enumeration = new ArrayList<>();
	private OWL2Datatype formulaDatatype;
	private String formula;

	private String ucum;
	private String score;

	public PhenotypeBean() {
	}

	public PhenotypeBean(Entity entity) {
		id = entity.getName();
		mainTitle = entity.getMainTitleText();
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

	public List<LocalizedString> getLabels() {
		return labels;
	}

	public void setLabels(List<LocalizedString> labels) {
		this.labels = labels;
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
}