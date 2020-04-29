package de.uni_leipzig.imise.onto_med.phenoman_editor.field;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocaleComboBoxModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.model.LocalizedStringTableModel;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.LocalizedString;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalizedStringField extends JPanel {
	private JPanel                    contentPane;
	private JTable                    table;
	private LocalizedStringTableModel model;
	private JButton                   addRowButton;
	private JButton                   deleteRowButton;

	public LocalizedStringField() {
		$$$setupUI$$$();
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
		model.setRows(data == null ? new ArrayList<>() : data);
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

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
		final JScrollPane scrollPane1 = new JScrollPane();
		contentPane.add(scrollPane1, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), new Dimension(-1, 100), new Dimension(-1, 200), 0, false));
		scrollPane1.setViewportView(table);
		addRowButton = new JButton();
		addRowButton.setText("Add row");
		contentPane.add(addRowButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		deleteRowButton = new JButton();
		deleteRowButton.setText("Delete row");
		deleteRowButton.setToolTipText("Click here to delete the selected row");
		contentPane.add(deleteRowButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		contentPane.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return contentPane;
	}
}
