package de.uni_leipzig.imise.onto_med.phenoman_editor.model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StringTableModel extends AbstractTableModel {
  private List<String> rows = new ArrayList<>();

  public StringTableModel() {}

  public List<String> getRows() {
    return rows;
  }

  public void setRows(List<String> rows) {
    this.rows = rows;
    fireTableDataChanged();
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
    return null;
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

  public void removeRow(int row) {
    if (rows.size() <= row) return;

    rows.remove(row);
    fireTableRowsDeleted(row, row);
  }
}
