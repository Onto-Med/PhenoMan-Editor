package de.uni_leipzig.imise.onto_med.phenoman_editor.model;

import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OWL2DatatypeComboBoxModel implements ComboBoxModel<OWL2Datatype> {
    private List<OWL2Datatype> datatypes;
    private int index = -1;

    public OWL2DatatypeComboBoxModel() {
        datatypes = Arrays.asList(OWL2Datatype.XSD_BOOLEAN, OWL2Datatype.XSD_DECIMAL, OWL2Datatype.XSD_DATE_TIME);
        datatypes.sort(Comparator.comparing(OWL2Datatype::getShortForm));
    }

    @Override
    public void setSelectedItem(Object o) {
        index = datatypes.indexOf(o);
    }

    @Override
    public OWL2Datatype getSelectedItem() {
        return index >= 0 ? datatypes.get(index) : null;
    }

    @Override
    public int getSize() {
        return datatypes.size();
    }

    @Override
    public OWL2Datatype getElementAt(int i) {
        return datatypes.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) { }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) { }
}
