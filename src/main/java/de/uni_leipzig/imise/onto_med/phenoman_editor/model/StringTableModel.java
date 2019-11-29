package de.uni_leipzig.imise.onto_med.phenoman_editor.model;

import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StringTableModel extends AbstractTableModel {
    private List<String> rows = new ArrayList<>();

    public StringTableModel() {}

    public StringTableModel(List<String> rows) {
        setRows(rows);
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
        fireTableDataChanged();
    }

    public List<String> getRows() {
        return rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return (col == 0) ? rows.get(row) : null;
    }

    public String getColumnName(int column) {
        return (column == 0) ? "String" : null;
    }

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            rows.set(row, (String) value);
        }
        fireTableCellUpdated(row, col);
    }

    public void addRow(String row) {
        rows.add(row);
        int index = getRowCount() - 1;
        fireTableRowsInserted(index, index);
    }
}
