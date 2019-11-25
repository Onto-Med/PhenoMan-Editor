package de.uni_leipzig.imise.onto_med.phenoman_editor;

import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.category_tree.EntityTreeNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.Callable;

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
    private JButton reloadButton;
    private PhenotypeForm phenotypeForm1;
    private JLabel copImage;
    private JLabel exampleImage;
    private PhenotypeManager model;

    public PhenoManEditor() {
        setIconImage(new ImageIcon(Objects.requireNonNull(PhenoManEditor.class.getClassLoader().getResource("images/favicon.png"))).getImage());
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
		});

		loadOntologyButton.addActionListener(actionEvent -> {
			String path = ontologyPath.getText();
			if (!path.isBlank()) {
				try {
				    doInBackground("Loading ontology...", () -> {
                        model = new PhenotypeManager(path, false);
                        tabbedPane.setEnabledAt(2, true);
                        tabbedPane.setSelectedIndex(2);
                        return null;
                    });
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(
							new JFrame(),
							String.format("Could not load ontology from file %s!\nReason: %s", path, e.getLocalizedMessage())
					);
				}
			}
		});
        reloadButton.addActionListener(actionEvent -> {
            EntityTreeNode node = model.getEntityTree(true);
            tree.setModel(new DefaultTreeModel(convertToTreeNode(node)));
        });
    }

    private MutableTreeNode convertToTreeNode(EntityTreeNode entityNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entityNode.getEntity());
        entityNode.getChildren().forEach(c -> node.add(convertToTreeNode(c)));
        return node;
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

    public void setData(PhenotypeBean data) {
        id.setText(data.getId());
        label.setText(data.getLabel());
        definition.setText(data.getDefinition());
    }

    public void getData(PhenotypeBean data) {
        data.setId(id.getText());
        data.setLabel(label.getText());
        data.setDefinition(definition.getText());
    }

    public boolean isModified(PhenotypeBean data) {
        if (id.getText() != null ? !id.getText().equals(data.getId()) : data.getId() != null)
            return true;
        if (label.getText() != null ? !label.getText().equals(data.getLabel()) : data.getLabel() != null)
            return true;
        if (definition.getText() != null ? !definition.getText().equals(data.getDefinition()) : data.getDefinition() != null)
            return true;
        return false;
    }

    private <T> void doInBackground(String dialog, Callable<T> function) {
        JFrame frame = new JFrame();
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(PhenoManEditor.class.getClassLoader().getResource("images/favicon.png"))).getImage());
        final JDialog dlgProgress = new JDialog(frame, dialog, true);
        JProgressBar  pbProgress  = new JProgressBar(0, 100);
        pbProgress.setIndeterminate(true);

        dlgProgress.getContentPane().add(BorderLayout.CENTER, pbProgress);
        dlgProgress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgProgress.setSize(300, 60);
        dlgProgress.setLocationRelativeTo(null);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                function.call();
                return null;
            }

            @Override
            protected void done() {
                dlgProgress.dispose();
            }
        };

        worker.execute();
        dlgProgress.setVisible(true);
        validate();
    }

    private void createUIComponents() {
        ClassLoader classLoader = PhenoManEditor.class.getClassLoader();
        copImage = new JLabel(new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/COP.png")).getPath()));
        exampleImage = new JLabel(new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/Example_BSA.png")).getPath()));
    }
}
