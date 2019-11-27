package de.uni_leipzig.imise.onto_med.phenoman_editor.model;

import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalizedStringTableModel extends AbstractTableModel {
    private List<LocalizedString> rows = new ArrayList<>();

    public LocalizedStringTableModel() {}

    public LocalizedStringTableModel(List<LocalizedString> rows) {
        setRows(rows);
    }

    public void setRows(List<LocalizedString> rows) {
        this.rows = rows;
    }

    public List<LocalizedString> getRows() {
        return rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return rows.get(row).getLocale();
        } else if (col == 1) {
            return rows.get(row).getString();
        }
        return null;
    }

    public String getColumnName(int column) {
        if (column == 0) return "Language";
        if (column == 1) return "String";
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            rows.get(row).setLocale((Locale) value);
        } else if (col == 1) {
            rows.get(row).setString((String) value);
        }
        fireTableCellUpdated(row, col);
    }

    public void addRow(LocalizedString row) {
        rows.add(row);
        int index = getRowCount() - 1;
        fireTableRowsInserted(index, index);
    }
}
