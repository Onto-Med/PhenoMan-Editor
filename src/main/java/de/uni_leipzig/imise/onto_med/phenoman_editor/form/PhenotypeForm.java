package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.LocalizedStringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.StringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.OWL2DatatypeComboBoxModel;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.phenotype.top_level.Entity;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

public class PhenotypeForm extends JPanel {
    private PhenotypeManager model;

    private JTextField idField;
    private JPanel contentPane;
    private JButton saveButton;
    private LocalizedStringField descriptionsField;
    private LocalizedStringField titlesField;
    private JTextField mainTitleField;
    private LocalizedStringField synonymsField;
    private JTextField ucumField;
    private JLabel superPhenotypeField;
    private JTextArea formulaField;
    private JFormattedTextField scoreField;
    private StringField relationsField;
    private StringField codesField;
    private JLabel superCategoriesField;
    private JComboBox datatypeField;
    private JComboBox formulaDatatypeField;

    public PhenotypeForm() {
        scoreField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("###0.00"))));
        saveButton.addActionListener(actionEvent -> {
            PhenotypeBean phenotype = new PhenotypeBean();
            getData(phenotype);
            System.out.println(phenotype);
        });
    }

    public void setData(PhenotypeBean data) {
        idField.setText(data.getId());
        mainTitleField.setText(data.getMainTitle());
        titlesField.setData(data.getTitles());
        synonymsField.setData(data.getSynonyms());
        descriptionsField.setData(data.getDescriptions());
        relationsField.setData(data.getRelations());

        codesField.setData(data.getCodes());
        datatypeField.getModel().setSelectedItem(data.getDatatype());

        ucumField.setText(data.getUcum());

        formulaDatatypeField.getModel().setSelectedItem(data.getFormulaDatatype());
        formulaField.setText(data.getFormula());
        scoreField.setValue(data.getScore());

        if (data.getSuperPhenotype() != null)
            superPhenotypeField.setText(data.getSuperPhenotype().getMainTitleText());
        if (data.getSuperCategories() != null)
            superCategoriesField.setText(data.getSuperCategories().stream().map(Entity::getMainTitleText).collect(Collectors.joining(", ")));
    }

    public void getData(PhenotypeBean data) {
        data.setId(idField.getText());
        data.setMainTitle(mainTitleField.getText());
        data.setTitles(titlesField.getData());
        data.setSynonyms(synonymsField.getData());
        data.setDescriptions(descriptionsField.getData());
        data.setRelations(relationsField.getData());
        data.setCodes(codesField.getData());
        data.setDatatype((OWL2Datatype) datatypeField.getSelectedItem());
        data.setUcum(ucumField.getText());
        data.setFormulaDatatype((OWL2Datatype) formulaDatatypeField.getSelectedItem());
        data.setFormula(formulaField.getText());
        data.setScore((BigDecimal) scoreField.getValue());
    }

    public boolean isModified(PhenotypeBean data) {
        if (idField.getText() != null ? !idField.getText().equals(data.getId()) : data.getId() != null)
            return true;
        if (mainTitleField.getText() != null ? !mainTitleField.getText().equals(data.getMainTitle()) : data.getMainTitle() != null)
            return true;
        if (titlesField.isModified(data.getTitles()))
            return true;
        if (synonymsField.isModified(data.getSynonyms()))
            return true;
        if (descriptionsField.isModified(data.getDescriptions()))
            return true;
        if (relationsField.isModified(data.getRelations()))
            return true;
        if (codesField.isModified(data.getCodes()))
            return true;
        if (datatypeField.getSelectedItem() != null ? !datatypeField.getSelectedItem().equals(data.getDatatype()) : data.getDatatype() != null)
            return true;
        if (formulaDatatypeField.getSelectedItem() != null ? !formulaDatatypeField.getSelectedItem().equals(data.getFormulaDatatype()) : data.getFormulaDatatype() != null)
            return true;
        if (ucumField.getText() != null ? !ucumField.getText().equals(data.getUcum()) : data.getUcum() != null)
            return true;
        if (scoreField.getValue() != null ? !scoreField.getValue().equals(data.getScore()) : data.getScore() != null)
            return true;
        if (formulaField.getText() != null ? !formulaField.getText().equals(data.getFormula()) : data.getFormula() != null)
            return true;
        return false;
    }

    private void createUIComponents() {
        datatypeField = new JComboBox<>(new OWL2DatatypeComboBoxModel());
        formulaDatatypeField = new JComboBox<>(new OWL2DatatypeComboBoxModel());
    }
}
