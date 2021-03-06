package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.imise.onto_api.entities.restrictions.data_range.DecimalRangeLimited;
import org.semanticweb.owlapi.vocab.OWLFacet;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

public class DataRangeField extends JPanel {
    private JPanel contentPane;
    private JComboBox<String> minOperatorComboBox;
    private JFormattedTextField minField;
    private JComboBox<String> maxOperatorComboBox;
    private JFormattedTextField maxField;

    public DataRangeField() {
        minField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("###0.00"))));
        maxField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("###0.00"))));
    }

    public void setData(DataRange data) {
        minOperatorComboBox.setSelectedItem(
                data.asDecimalRangeLimited().getMinFacet() != null
                        ? data.asDecimalRangeLimited().getMinFacet().getSymbolicForm()
                        : null
        );
        minField.setValue(data.asDecimalRangeLimited().getMinValue());
        maxOperatorComboBox.setSelectedItem(
                data.asDecimalRangeLimited().getMaxFacet() != null
                        ? data.asDecimalRangeLimited().getMaxFacet().getSymbolicForm()
                        : null
        );
        maxField.setValue(data.asDecimalRangeLimited().getMaxValue());
    }

    public DataRange getData() {
        DecimalRangeLimited range = new DecimalRangeLimited();
        if (minField.getValue() != null && minOperatorComboBox.getSelectedItem() != null)
            range.setLimit(
                    Objects.requireNonNull(OWLFacet.getFacetBySymbolicName((String) minOperatorComboBox.getSelectedItem())),
                    String.valueOf(minField.getValue())
            );
        if (maxField.getValue() != null && maxOperatorComboBox.getSelectedItem() != null)
            range.setLimit(
                    Objects.requireNonNull(OWLFacet.getFacetBySymbolicName((String) maxOperatorComboBox.getSelectedItem())),
                    String.valueOf(maxField.getValue())
            );
        return range;
    }

    public void setVisible(boolean visible) {
        contentPane.setVisible(visible);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        minOperatorComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement(">");
        defaultComboBoxModel1.addElement(">=");
        defaultComboBoxModel1.addElement("==");
        defaultComboBoxModel1.addElement("<=");
        defaultComboBoxModel1.addElement("<");
        minOperatorComboBox.setModel(defaultComboBoxModel1);
        contentPane.add(minOperatorComboBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        minField = new JFormattedTextField();
        contentPane.add(minField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        maxOperatorComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement(">");
        defaultComboBoxModel2.addElement(">=");
        defaultComboBoxModel2.addElement("==");
        defaultComboBoxModel2.addElement("<");
        defaultComboBoxModel2.addElement("<=");
        maxOperatorComboBox.setModel(defaultComboBoxModel2);
        contentPane.add(maxOperatorComboBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxField = new JFormattedTextField();
        contentPane.add(maxField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
