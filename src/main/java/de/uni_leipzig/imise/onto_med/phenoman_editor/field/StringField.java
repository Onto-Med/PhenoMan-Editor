package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import de.uni_leipzig.imise.onto_med.phenoman_editor.model.StringTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StringField extends JPanel {
    private JPanel contentPane;
    private JTable table;
    private StringTableModel model;
    private JButton addRowButton;

    public StringField() {
        addRowButton.addActionListener(actionEvent -> {
            model.addRow(null);
        });
    }

    public void setData(List<String> data) {
        model.setRows(Objects.requireNonNullElseGet(data, ArrayList::new));
    }

    public List<String> getData() {
        return model.getRows();
    }

    public boolean isModified(List<String> data) {
        return model.getRows() != null ? !model.getRows().equals(data) : data != null;
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private void createUIComponents() {
        model = new StringTableModel();
        table = new JTable(model);
    }
}