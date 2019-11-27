package de.uni_leipzig.imise.onto_med.phenoman_editor.model;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LocaleComboBoxModel implements ComboBoxModel<Locale> {
    private List<Locale> locales;
    private int index = -1;

    public LocaleComboBoxModel() {
        locales = Arrays.asList(Locale.getAvailableLocales());
        locales.sort(Comparator.comparing(Locale::getLanguage));
    }

    @Override
    public void setSelectedItem(Object o) {
        index = locales.indexOf(o);
    }

    @Override
    public Locale getSelectedItem() {
        return index >= 0 ? locales.get(index) : null;
    }

    @Override
    public int getSize() {
        return locales.size();
    }

    @Override
    public Locale getElementAt(int i) {
        return locales.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) { }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) { }
}
