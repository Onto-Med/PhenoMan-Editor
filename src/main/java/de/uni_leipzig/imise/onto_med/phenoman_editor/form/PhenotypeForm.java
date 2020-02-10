package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.DataRangeField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.LocalizedStringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.StringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.OWL2DatatypeComboBoxModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.smith.phenoman.man.PhenotypeManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

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
    private JTextField superCategoriesField;
    private JComboBox<OWL2Datatype> datatypeField;
    private JLabel rangeLabel;
    private JLabel scoreLabel;
    private JLabel ucumLabel;
    private JLabel formulaLabel;
    private JLabel datatypeLabel;
    private JLabel superPhenotypeLabel;
    private JLabel superCategoriesLabel;
    private JLabel codesLabel;
    private DataRangeField rangeField;
    private JCheckBox negatedCheckBox;
    private JLabel negatedLabel;
    private JXCollapsiblePane metadataCollapsiblePane;
    private JButton showAdditionalMetadataButton;
    private JScrollPane formulaScrollPane;
    private EntityType type;
    private ActionListener listener;

    public PhenotypeForm(ActionListener listener) {
        this.listener = listener;
        scoreField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("###0.00"))));
        IconFontSwing.register(FontAwesome.getIconFont());
        saveButton.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 12));
        saveButton.addActionListener(actionEvent -> {
            if (model == null) return;

            try {
                getData(new PhenotypeBean()).addToModel(model);
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "phenotype_saved"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        new JFrame(), String.format("Could not save phenotype!\nReason: %s", e.getLocalizedMessage())
                );
            }
        });
        showAdditionalMetadataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAdditionalMetadataButton.setText((!metadataCollapsiblePane.isCollapsed() ? "Show" : "Hide") + " additional metadata");
            }
        });
    }

    public void setModel(PhenotypeManager model) {
        this.model = model;
    }

    public void setData(PhenotypeBean data) {
        type = data.getType();
        idField.setText(data.getId());
        mainTitleField.setText(data.getMainTitle());
        titlesField.setData(data.getTitles());
        synonymsField.setData(data.getSynonyms());
        descriptionsField.setData(data.getDescriptions());
        relationsField.setData(data.getRelations());
        negatedCheckBox.setSelected(data.getNegated() != null ? data.getNegated() : false);

        if (data.getSuperCategories() != null)
            superCategoriesField.setText(String.join("; ", data.getSuperCategories()));

        if (type.isAbstractPhenotype()) {
            ucumField.setText(data.getUcum());
            codesField.setData(data.getCodes());
        }

        if (type.hasFormula()) formulaField.setText(data.getFormula());
        if (type.hasDatatype()) datatypeField.getModel().setSelectedItem(data.getDatatype());

        if (type.isRestrictedPhenotype()) {
            scoreField.setValue(data.getScore());
            codesField.setData(data.getCodes());
            if (data.getSuperPhenotype() != null)
                superPhenotypeField.setText(data.getSuperPhenotype());
            if (data.getRestriction() != null)
                rangeField.setData(data.getRestriction().asDecimalRangeLimited());
        }

        toggleFields();
    }

    public PhenotypeBean getData(PhenotypeBean data) {
        data.setType(type);
        data.setId(idField.getText());
        data.setMainTitle(mainTitleField.getText());
        data.setSuperCategories(Arrays.asList(superCategoriesField.getText().split(";")));
        data.setSuperPhenotype(superPhenotypeField.getText());
        data.setTitles(titlesField.getData());
        data.setSynonyms(synonymsField.getData());
        data.setDescriptions(descriptionsField.getData());
        data.setRelations(relationsField.getData());
        data.setCodes(codesField.getData());
        data.setDatatype((OWL2Datatype) datatypeField.getSelectedItem());
        data.setUcum(ucumField.getText());
        data.setFormula(formulaField.getText());
        if (scoreField.getValue() != null) data.setScore(new BigDecimal(scoreField.getText().replace(",", ".")));
        data.setRestriction(rangeField.getData());
        data.setNegated(negatedCheckBox.isSelected());
        return data;
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
        if (ucumField.getText() != null ? !ucumField.getText().equals(data.getUcum()) : data.getUcum() != null)
            return true;
        if (scoreField.getValue() != null ? !scoreField.getValue().equals(data.getScore()) : data.getScore() != null)
            return true;
        if (formulaField.getText() != null ? !formulaField.getText().equals(data.getFormula()) : data.getFormula() != null)
            return true;
        if (rangeField.getData() != null ? !rangeField.getData().equals(data.getRestriction()) : data.getRestriction() != null)
            return true;
        if (negatedCheckBox.isSelected() != data.getNegated())
            return true;
        return false;
    }

    public void setVisible(boolean visible) {
        contentPane.setVisible(visible);
    }

    private void toggleFields() {
        rangeLabel.setVisible(type.hasRestriction());
        rangeField.setVisible(type.hasRestriction());
        superPhenotypeLabel.setVisible(type.isRestrictedPhenotype());
        superPhenotypeField.setVisible(type.isRestrictedPhenotype());
        scoreLabel.setVisible(type.isRestrictedPhenotype());
        scoreField.setVisible(type.isRestrictedPhenotype());
        superCategoriesLabel.setVisible(type.isAbstractPhenotype());
        superCategoriesField.setVisible(type.isAbstractPhenotype());
        datatypeLabel.setVisible(type.hasDatatype());
        datatypeField.setVisible(type.hasDatatype());
        formulaScrollPane.setVisible(type.hasFormula());
        formulaLabel.setVisible(type.hasFormula());
        formulaField.setVisible(type.hasFormula());
        ucumLabel.setVisible(type.equals(EntityType.ABSTRACT_SINGLE_PHENOTYPE));
        ucumField.setVisible(type.equals(EntityType.ABSTRACT_SINGLE_PHENOTYPE));
        codesLabel.setVisible(!type.equals(EntityType.CATEGORY));
        codesField.setVisible(!type.equals(EntityType.CATEGORY));
        negatedLabel.setVisible(type.isRestrictedPhenotype());
        negatedCheckBox.setVisible(type.isRestrictedPhenotype());
        metadataCollapsiblePane.setCollapsed(true);
        showAdditionalMetadataButton.setText("Show additional metadata");
    }

    private void createUIComponents() {
        datatypeField = new JComboBox<>(new OWL2DatatypeComboBoxModel());
        metadataCollapsiblePane = new JXCollapsiblePane();
        metadataCollapsiblePane.setCollapsed(true);
        showAdditionalMetadataButton = new JButton(metadataCollapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
    }
}
