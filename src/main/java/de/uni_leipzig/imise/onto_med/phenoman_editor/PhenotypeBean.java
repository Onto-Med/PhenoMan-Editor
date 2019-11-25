package de.uni_leipzig.imise.onto_med.phenoman_editor;

public class PhenotypeBean {
	private String id;
	private String label;
	private String definition;

	public PhenotypeBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(final String definition) {
		this.definition = definition;
	}
}