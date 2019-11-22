package de.uni_leipzig.imise.onto_med.phenoman_editor;

import org.smith.phenoman.man.PhenotypeManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class PhenoManEditor extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private JTree tree;
    private JTextField id;
    private JTextField label;
    private JTextArea definition;
    private JTextField ontologyPath;
    private JButton browseButton;
	private JButton loadOntologyButton;
	private JPanel editorPane;
	private PhenotypeManager model;

    public PhenoManEditor() {
		add(contentPane);
		setTitle("PhenoMan-Editor");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(700, 600);
		setLocationRelativeTo(null);
		tabbedPane.setEnabledAt(2, false);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() != 2) return;

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node == null) return;
                Object nodeInfo = node.getUserObject();
            }
        });
        browseButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Ontology", "xml", "owl"));
            if (fileChooser.showOpenDialog(new JFrame()) != JFileChooser.APPROVE_OPTION) return;

            File file = fileChooser.getSelectedFile();
			ontologyPath.setText(file.getAbsolutePath());
			loadOntologyButton.setEnabled(true);
		});

		loadOntologyButton.addActionListener(actionEvent -> {
			String path = ontologyPath.getText();
			if (!path.isBlank()) {
				try {
					model = new PhenotypeManager(path, false);
					tabbedPane.setEnabledAt(2, true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(
							new JFrame(),
							String.format("Could not load ontology from file %s!\nReason: %s", path, e.getLocalizedMessage())
					);
				}
			}
		});
	}

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(() -> {
            try {
                new PhenoManEditor().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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
