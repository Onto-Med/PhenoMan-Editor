package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocaleComboBoxModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocalizedStringTableModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalizedStringField extends JPanel {
    private JPanel contentPane;
    private JTable table;
    private LocalizedStringTableModel model;
    private JButton addRowButton;
    private JButton deleteRowButton;

    public LocalizedStringField() {
        IconFontSwing.register(FontAwesome.getIconFont());
        addRowButton.setIcon(IconFontSwing.buildIcon(FontAwesome.PLUS, 12));
        addRowButton.addActionListener(actionEvent -> model.addRow(new LocalizedString()));
        deleteRowButton.setIcon(IconFontSwing.buildIcon(FontAwesome.TRASH_O, 12));
        deleteRowButton.addActionListener(actionEvent -> {
            if (table.getSelectedRow() == -1) return;
            model.removeRow(table.getSelectedRow());
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

    public void setVisible(boolean visible) {
        contentPane.setVisible(visible);
    }

    private void createUIComponents() {
        model = new LocalizedStringTableModel();
        table = new JTable(model);
        TableColumn localeColumn = table.getColumnModel().getColumn(0);
        localeColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(new LocaleComboBoxModel())));
    }
}
