package de.uni_leipzig.imise.onto_med.phenoman_editor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OntologySelector {
	private JTabbedPane tabbedPane1;
	private JPanel      panel1;
	private JTree       tree;
	private JTextField  id;
	private JTextField  label;
	private JTextArea   definition;
	private JTextField  textField1;
	private JButton     browseButton;

	public OntologySelector() {
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() != 2) return;

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node == null) return;
				Object nodeInfo = node.getUserObject();
			}
		});
	}

	public void setData(Phenotype data) {
		id.setText(data.getId());
		label.setText(data.getLabel());
		definition.setText(data.getDefinition());
	}

	public void getData(Phenotype data) {
		data.setId(id.getText());
		data.setLabel(label.getText());
		data.setDefinition(definition.getText());
	}

	public boolean isModified(Phenotype data) {
		if (id.getText() != null ? !id.getText().equals(data.getId()) : data.getId() != null)
			return true;
		if (label.getText() != null ? !label.getText().equals(data.getLabel()) : data.getLabel() != null)
			return true;
		if (definition.getText() != null ? !definition.getText().equals(data.getDefinition()) : data.getDefinition() != null)
			return true;
		return false;
	}
}
