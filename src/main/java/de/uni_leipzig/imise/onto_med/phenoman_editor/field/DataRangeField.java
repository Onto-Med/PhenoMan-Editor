package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import de.imise.onto_api.entities.restrictions.data_range.DataRange;
import de.imise.onto_api.entities.restrictions.data_range.DecimalRangeLimited;
import org.semanticweb.owlapi.vocab.OWLFacet;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
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
}
