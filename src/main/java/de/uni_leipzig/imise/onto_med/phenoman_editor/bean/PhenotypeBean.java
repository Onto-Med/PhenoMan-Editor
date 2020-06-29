package de.uni_leipzig.imise.onto_med.phenoman_editor.bean;

import care.smith.phep.phenoman.core.model.function.Function;
import care.smith.phep.phenoman.core.model.resource_type.ResourceType;
import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhenotypeBean {
  /**
   * Describes, which type the phenotype is of. (@Code{EntityType}) e.g. AbstractSinglePhenotype,
   * RestrictedBooleanPhenotype, etc.
   */
  private EntityType type;

  /** Unique identifier of the phenotype. Equivalent to {@code Phenotype.name} field. */
  private String id;

  private String oid;
  private String mainTitle;

  private List<LocalizedString> titles = new ArrayList<>();
  private List<LocalizedString> synonyms = new ArrayList<>();
  private List<LocalizedString> descriptions = new ArrayList<>();

  private List<String> relations = new ArrayList<>();
  private List<String> codes = new ArrayList<>();
  private List<String> superCategories = new ArrayList<>();

  private Boolean exclusionCriterion;

  /**
   * A FHIR resource type, this phenotype is stored as.
   */
  private ResourceType resourceType;

  /**
   * If there are multiple values present for a phenotype, {@code function} determines which of those values
   * should be used for calculations, etc.
   * See {@link care.smith.phep.phenoman.core.model.function.Functions} for supported functions.
   */
  private Function function;

  /**
   * If true, the phenotype is included in the property list of a FHIR query result set
   * and it's values can be accessed.
   */
  private Boolean inProjection;


  /**
   * OWL2Datatype of the {@code AbstractSinglePhenotype} or of the formula of the {@code
   * AbstractCalculationPhenotype}.
   */
  private OWL2Datatype datatype;

  private List<String> units = new ArrayList<>();
  private BigDecimal score;

  /** The {@code AbstractPhenotype} a {@code RestrictedPhenotype} is related to. */
  private String superPhenotype;

  /** Specifies if the phenotype is negated. */
  private Boolean negated;

  /**
   * The formula of a {@code RestrictedBooleanPhenotype} or {@code AbstractCalculationPhenotype}.
   */
  private String formula;

  private DataRange restriction;
  private Boolean mainResult;

  public PhenotypeBean() {}

  public EntityType getType() {
    return type;
  }

  public PhenotypeBean setType(EntityType type) {
    this.type = type;
    return this;
  }

  public String getId() {
    if (id == null || id.isEmpty()) id = UUID.randomUUID().toString();
    return id;
  }

  public PhenotypeBean setId(final String id) {
    this.id = id;
    return this;
  }

  public String getOid() {
    return oid;
  }

  public PhenotypeBean setOid(String oid) {
    this.oid = oid;
    return this;
  }

  public String getMainTitle() {
    return mainTitle;
  }

  public PhenotypeBean setMainTitle(String mainTitle) {
    this.mainTitle = mainTitle;
    return this;
  }

  public List<LocalizedString> getTitles() {
    return titles;
  }

  public PhenotypeBean setTitles(List<LocalizedString> titles) {
    this.titles = titles;
    return this;
  }

  public List<LocalizedString> getSynonyms() {
    return synonyms;
  }

  public PhenotypeBean setSynonyms(List<LocalizedString> synonyms) {
    this.synonyms = synonyms;
    return this;
  }

  public List<LocalizedString> getDescriptions() {
    return descriptions;
  }

  public PhenotypeBean setDescriptions(List<LocalizedString> descriptions) {
    this.descriptions = descriptions;
    return this;
  }

  public List<String> getRelations() {
    return relations;
  }

  public PhenotypeBean setRelations(List<String> relations) {
    this.relations = relations;
    return this;
  }

  public List<String> getCodes() {
    return codes;
  }

  public PhenotypeBean setCodes(List<String> codes) {
    this.codes = codes;
    return this;
  }

  public Boolean getExclusionCriterion() {
    return exclusionCriterion;
  }

  public PhenotypeBean setExclusionCriterion(Boolean exclusionCriterion) {
    this.exclusionCriterion = exclusionCriterion;
    return this;
  }

  public ResourceType getResourceType() {
    return resourceType;
  }

  public PhenotypeBean setResourceType(ResourceType resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  public Function getFunction() {
    return function;
  }

  public PhenotypeBean setFunction(Function function) {
    this.function = function;
    return this;
  }

  public Boolean getInProjection() {
    return inProjection;
  }

  public PhenotypeBean setInProjection(Boolean inProjection) {
    this.inProjection = inProjection;
    return this;
  }

  public OWL2Datatype getDatatype() {
    return datatype;
  }

  public PhenotypeBean setDatatype(OWL2Datatype datatype) {
    this.datatype = datatype;
    return this;
  }

  public String getSuperPhenotype() {
    return superPhenotype;
  }

  public PhenotypeBean setSuperPhenotype(String superPhenotype) {
    this.superPhenotype = superPhenotype;
    return this;
  }

  public List<String> getSuperCategories() {
    return superCategories;
  }

  public PhenotypeBean setSuperCategories(List<String> superCategories) {
    this.superCategories = superCategories;
    return this;
  }

  public String getFormula() {
    return formula;
  }

  public PhenotypeBean setFormula(String formula) {
    this.formula = formula;
    return this;
  }

  public BigDecimal getScore() {
    return score;
  }

  public PhenotypeBean setScore(BigDecimal score) {
    this.score = score;
    return this;
  }

  public List<String> getUnits() {
    return units;
  }

  public PhenotypeBean setUnits(List<String> units) {
    this.units = units;
    return this;
  }

  public DataRange getRestriction() {
    return restriction;
  }

  public PhenotypeBean setRestriction(DataRange restriction) {
    this.restriction = restriction;
    return this;
  }

  public Boolean getNegated() {
    return negated;
  }

  public PhenotypeBean setNegated(Boolean negated) {
    this.negated = negated;
    return this;
  }

  public Boolean getMainResult() {
    return mainResult == null ? false : mainResult;
  }

  public PhenotypeBean setMainResult(Boolean mainResult) {
    this.mainResult = mainResult;
    return this;
  }
}
