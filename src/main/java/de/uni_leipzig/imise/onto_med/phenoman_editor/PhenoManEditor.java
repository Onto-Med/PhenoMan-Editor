package de.uni_leipzig.imise.onto_med.phenoman_editor;

import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.form.PhenotypeForm;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeTree;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.category_tree.EntityTreeNode;
import org.smith.phenoman.model.phenotype.top_level.Entity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.Callable;

public class PhenoManEditor extends JFrame implements ChangeListener {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private JTextField ontologyPath;
    private JButton browseButton;
	private JButton loadOntologyButton;
	private JPanel editorPane;
    private JButton reloadButton;
    private PhenotypeForm phenotypeForm;
    private JLabel copImage;
    private JLabel exampleImage;
    private PhenotypeTree tree;
    private JTextField treeSearchField;
    private JScrollPane phenotypeFormScrollPane;
    private JScrollPane introductionScrollPane;
    private PhenotypeManager model;

    public PhenoManEditor() {
        IconFontSwing.register(FontAwesome.getIconFont());
        setIconImage(new ImageIcon(Objects.requireNonNull(PhenoManEditor.class.getClassLoader().getResource("images/favicon.png"))).getImage());
		add(contentPane);
		setTitle("PhenoMan-Editor");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);
        tabbedPane.setIconAt(0, IconFontSwing.buildIcon(FontAwesome.INFO, 12));
        tabbedPane.setIconAt(1, IconFontSwing.buildIcon(FontAwesome.SITEMAP, 12));
        tabbedPane.setIconAt(2, IconFontSwing.buildIcon(FontAwesome.PENCIL_SQUARE_O, 12));
        tabbedPane.setIconAt(3, IconFontSwing.buildIcon(FontAwesome.COGS, 12));
		tabbedPane.setEnabledAt(2, false);
        phenotypeForm.setVisible(false);
        phenotypeFormScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        introductionScrollPane.getVerticalScrollBar().setUnitIncrement(10);

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
                        phenotypeForm.setModel(model);
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
		reloadButton.setIcon(IconFontSwing.buildIcon(FontAwesome.REFRESH, 12));
        reloadButton.addActionListener(actionEvent -> {
            if (model == null) return;
            EntityTreeNode node = model.getEntityTree(true);
            tree.setModel(new DefaultTreeModel(convertToTreeNode(node)));
        });
        treeSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) return;
                if (treeSearchField.getText().isBlank()) return;

                Object root = tree.getModel().getRoot();
                if (root == null) return;

                Enumeration<TreeNode> enumeration = ((DefaultMutableTreeNode) root).depthFirstEnumeration();
                while (enumeration.hasMoreElements()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
                    if (node.toString().toLowerCase().contains(treeSearchField.getText().toLowerCase())) {
                        TreePath path = new TreePath(node.getPath());
                        tree.setSelectionPath(path);
                        tree.scrollPathToVisible(path);
                        break;
                    }
                }
            }
        });
    }

    private MutableTreeNode convertToTreeNode(EntityTreeNode entityNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entityNode.getName(), true);
        node.setUserObject(entityNode.getEntity());
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
        tree = new PhenotypeTree(this);
        tree.setModel(new DefaultTreeModel(null));
        tree.setShowsRootHandles(true);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        PhenotypeBean phenotype = new PhenotypeBean((Entity) ((DefaultMutableTreeNode) changeEvent.getSource()).getUserObject());
        phenotypeForm.setData(phenotype);
        phenotypeForm.setVisible(true);
    }
}
