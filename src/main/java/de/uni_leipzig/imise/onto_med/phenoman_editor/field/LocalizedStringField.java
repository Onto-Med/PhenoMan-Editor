package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocaleComboBoxModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocalizedStringTableModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class LocalizedStringField extends JPanel {
    private JPanel contentPane;
    private JTable table;
    private LocalizedStringTableModel model;
    private JButton addRowButton;

    public LocalizedStringField() {
        addRowButton.addActionListener(actionEvent -> {
            model.addRow(new LocalizedString(null, Locale.ENGLISH));
        });
    }

    public void setData(List<LocalizedString> data) {
        model.setRows(Objects.requireNonNullElseGet(data, ArrayList::new));
    }

    public List<LocalizedString> getData() {
        return model.getRows();
    }

    public boolean isModified(List<LocalizedString> data) {
        return model.getRows() != null ? !model.getRows().equals(data) : data != null;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private void createUIComponents() {
        model = new LocalizedStringTableModel();
        table = new JTable(model);
        TableColumn localeColumn = table.getColumnModel().getColumn(0);
        localeColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(new LocaleComboBoxModel())));
    }
}
