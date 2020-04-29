package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.DataRangeField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.LocalizedStringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.StringField;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.OWL2DatatypeComboBoxModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeManagerMapper;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

public class PhenotypeForm extends JPanel {
    private PhenotypeManagerMapper mapper;

    private JTextField              idField;
    private JPanel                  contentPane;
    private JButton                 saveButton;
    private LocalizedStringField    descriptionsField;
    private LocalizedStringField    titlesField;
    private JTextField              mainTitleField;
    private LocalizedStringField    synonymsField;
    private JTextField              unitsField;
    private JLabel                  superPhenotypeField;
    private JTextArea               formulaField;
    private JFormattedTextField     scoreField;
    private StringField             relationsField;
    private StringField             codesField;
    private JTextField              superCategoriesField;
    private JComboBox<OWL2Datatype> datatypeField;
    private JLabel                  rangeLabel;
    private JLabel                  scoreLabel;
    private JLabel                  ucumLabel;
    private JLabel                  formulaLabel;
    private JLabel                  datatypeLabel;
    private JLabel                  superPhenotypeLabel;
    private JLabel                  superCategoriesLabel;
    private JLabel                  codesLabel;
    private DataRangeField          rangeField;
    private JCheckBox               negatedCheckBox;
    private JLabel                  negatedLabel;
    private JXCollapsiblePane       metadataCollapsiblePane;
    private JButton                 showAdditionalMetadataButton;
    private JScrollPane             formulaScrollPane;
    private EntityType              type;

    public PhenotypeForm(ActionListener listener) {
        $$$setupUI$$$();
        scoreField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("###0.00"))));
        IconFontSwing.register(FontAwesome.getIconFont());
        saveButton.setIcon(IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 12));
        saveButton.addActionListener(actionEvent -> {
            if (mapper == null || !mapper.hasModel()) return;
            try {
                mapper.writeToOntology(mapper.buildEntity(getData(new PhenotypeBean())));
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "phenotype_saved"));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    new JFrame(), String.format("Could not save phenotype!\nReason: %s", e.getLocalizedMessage())
                );
            }
        });
        showAdditionalMetadataButton.addActionListener(actionEvent ->
            showAdditionalMetadataButton.setText((!metadataCollapsiblePane.isCollapsed() ? "Show" : "Hide") + " additional metadata")
        );
    }

    public void setMapper(PhenotypeManagerMapper mapper) {
        this.mapper = mapper;
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
            unitsField.setText(String.join(";", data.getUnits()));
            codesField.setData(data.getCodes());
        }

        if (type.hasFormula()) formulaField.setText(data.getFormula());
        if (type.hasDatatype()) {
            datatypeField.getModel().setSelectedItem(data.getDatatype());
            datatypeField.intervalAdded(null);
        }

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
        if (!superCategoriesField.getText().isEmpty())
            data.setSuperCategories(Arrays.asList(superCategoriesField.getText().split(";")));
        data.setSuperPhenotype(superPhenotypeField.getText());
        data.setTitles(titlesField.getData());
        data.setSynonyms(synonymsField.getData());
        data.setDescriptions(descriptionsField.getData());
        data.setRelations(relationsField.getData());
        data.setCodes(codesField.getData());
        data.setDatatype((OWL2Datatype) datatypeField.getSelectedItem());
        if (!unitsField.getText().isEmpty())
            data.setUnits(Arrays.asList(unitsField.getText().split(";")));
        data.setFormula(formulaField.getText());
        if (scoreField.getValue() != null) data.setScore(new BigDecimal(scoreField.getText().replace(",", ".")));
        data.setRestriction(rangeField.getData());
        data.setNegated(negatedCheckBox.isSelected());
        return data;
    }

    @SuppressWarnings("unused")
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
        if (unitsField.getText() != null ? !unitsField.getText().equals(String.join(";", data.getUnits())) : data.getUnits() != null)
            return true;
        if (superCategoriesField.getText() != null ? !superCategoriesField.getText().equals(String.join(";", data.getSuperCategories())) : data.getSuperPhenotype() != null)
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
        formulaLabel.setVisible(type.hasFormula());
        formulaScrollPane.setVisible(type.hasFormula());
        ucumLabel.setVisible(type.equals(EntityType.ABSTRACT_SINGLE_PHENOTYPE));
        unitsField.setVisible(type.equals(EntityType.ABSTRACT_SINGLE_PHENOTYPE));
        codesLabel.setVisible(!type.equals(EntityType.CATEGORY));
        codesField.setVisible(!type.equals(EntityType.CATEGORY));
        negatedLabel.setVisible(type.isRestrictedPhenotype());
        negatedCheckBox.setVisible(type.isRestrictedPhenotype());
        metadataCollapsiblePane.setCollapsed(true);
        showAdditionalMetadataButton.setText("Show additional metadata");
    }

    private void createUIComponents() {
        datatypeField           = new JComboBox<>(new OWL2DatatypeComboBoxModel());
        metadataCollapsiblePane = new JXCollapsiblePane();
        metadataCollapsiblePane.setCollapsed(true);
        showAdditionalMetadataButton = new JButton(metadataCollapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(16, 4, new Insets(5, 5, 5, 5), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("ID:*");
        contentPane.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        idField = new JTextField();
        contentPane.add(idField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Main Title:");
        contentPane.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        contentPane.add(saveButton, new GridConstraints(14, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mainTitleField = new JTextField();
        contentPane.add(mainTitleField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        superPhenotypeLabel = new JLabel();
        superPhenotypeLabel.setText("Super phenotype:");
        contentPane.add(superPhenotypeLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        superPhenotypeField = new JLabel();
        superPhenotypeField.setText("");
        contentPane.add(superPhenotypeField, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formulaLabel = new JLabel();
        formulaLabel.setText("Formula:*");
        contentPane.add(formulaLabel, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        superCategoriesLabel = new JLabel();
        superCategoriesLabel.setText("Super categories:");
        contentPane.add(superCategoriesLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        superCategoriesField = new JTextField();
        superCategoriesField.setText("");
        superCategoriesField.setToolTipText("Please use semicola (\";\") to separate multiple categories.");
        contentPane.add(superCategoriesField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        datatypeLabel = new JLabel();
        datatypeLabel.setText("Datatype:*");
        contentPane.add(datatypeLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPane.add(datatypeField, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rangeLabel = new JLabel();
        rangeLabel.setText("Restriction:*");
        contentPane.add(rangeLabel, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPane.add(separator1, new GridConstraints(7, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rangeField = new DataRangeField();
        contentPane.add(rangeField.$$$getRootComponent$$$(), new GridConstraints(10, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        negatedLabel = new JLabel();
        negatedLabel.setText("Negated:");
        contentPane.add(negatedLabel, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        negatedCheckBox = new JCheckBox();
        negatedCheckBox.setText("check this box to negate the restriction above");
        contentPane.add(negatedCheckBox, new GridConstraints(11, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scoreLabel = new JLabel();
        scoreLabel.setText("Score:");
        contentPane.add(scoreLabel, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scoreField = new JFormattedTextField();
        contentPane.add(scoreField, new GridConstraints(13, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ucumLabel = new JLabel();
        ucumLabel.setText("Units:");
        contentPane.add(ucumLabel, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unitsField = new JTextField();
        contentPane.add(unitsField, new GridConstraints(12, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        metadataCollapsiblePane.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(metadataCollapsiblePane, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Titles:");
        metadataCollapsiblePane.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 16), null, 0, false));
        titlesField = new LocalizedStringField();
        metadataCollapsiblePane.add(titlesField.$$$getRootComponent$$$(), new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Synonyms:");
        metadataCollapsiblePane.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 16), null, 0, false));
        synonymsField = new LocalizedStringField();
        metadataCollapsiblePane.add(synonymsField.$$$getRootComponent$$$(), new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Descriptions:");
        metadataCollapsiblePane.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 16), null, 0, false));
        descriptionsField = new LocalizedStringField();
        metadataCollapsiblePane.add(descriptionsField.$$$getRootComponent$$$(), new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Relations:");
        metadataCollapsiblePane.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 16), null, 0, false));
        relationsField = new StringField();
        metadataCollapsiblePane.add(relationsField.$$$getRootComponent$$$(), new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        codesLabel = new JLabel();
        codesLabel.setText("Codes:");
        contentPane.add(codesLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        codesField = new StringField();
        contentPane.add(codesField.$$$getRootComponent$$$(), new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        showAdditionalMetadataButton.setText("Show additional metadata");
        contentPane.add(showAdditionalMetadataButton, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setEnabled(false);
        label7.setText("* Required fields");
        contentPane.add(label7, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        formulaScrollPane = new JScrollPane();
        contentPane.add(formulaScrollPane, new GridConstraints(9, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        formulaField = new JTextArea();
        formulaField.setLineWrap(true);
        formulaField.setWrapStyleWord(true);
        formulaScrollPane.setViewportView(formulaField);
        label1.setLabelFor(idField);
        label2.setLabelFor(mainTitleField);
        formulaLabel.setLabelFor(formulaField);
        superCategoriesLabel.setLabelFor(superCategoriesField);
        datatypeLabel.setLabelFor(datatypeField);
        scoreLabel.setLabelFor(scoreField);
        ucumLabel.setLabelFor(unitsField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
