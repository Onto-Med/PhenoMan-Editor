package de.uni_leipzig.imise.onto_med.phenoman_editor;

import care.smith.phep.phenoman.core.man.PhenotypeManager;
import care.smith.phep.phenoman.core.model.category_tree.EntityTreeNode;
import care.smith.phep.phenoman.core.model.phenotype.top_level.Entity;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.form.PhenotypeForm;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeManagerMapper;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeTree;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.Callable;

public class PhenoManEditor extends JFrame implements ActionListener {
    private JTabbedPane            tabbedPane;
    private JPanel                 contentPane;
    private JTextField             ontologyPath;
    private JButton                browseButton;
    private JButton                loadOntologyButton;
    private JButton                reloadButton;
    private PhenotypeForm          phenotypeForm;
    private PhenotypeTree          tree;
    private JTextField             treeSearchField;
    private JScrollPane            phenotypeFormScrollPane;
    private JScrollPane            introductionScrollPane;
    private PhenotypeManagerMapper mapper;

    @SuppressWarnings("unused")
    private JLabel copImage;
    @SuppressWarnings("unused")
    private JLabel exampleImage;

    public PhenoManEditor() {
        $$$setupUI$$$();
        IconFontSwing.register(FontAwesome.getIconFont());

        ClassLoader classLoader = PhenoManEditor.class.getClassLoader();
        setIconImage(new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/favicon.png"))).getImage());
        copImage.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/COP.png"))));
        exampleImage.setIcon(new ImageIcon(Objects.requireNonNull(classLoader.getResource("images/Example_BSA.png"))));

		add(contentPane);
		setTitle("PhenoMan-Editor");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setLocationRelativeTo(null);
        tabbedPane.setIconAt(0, IconFontSwing.buildIcon(FontAwesome.INFO, 12));
        tabbedPane.setIconAt(1, IconFontSwing.buildIcon(FontAwesome.SITEMAP, 12));
        tabbedPane.setIconAt(2, IconFontSwing.buildIcon(FontAwesome.PENCIL_SQUARE_O, 12));
        tabbedPane.setIconAt(3, IconFontSwing.buildIcon(FontAwesome.BOLT, 12));
        tabbedPane.setIconAt(4, IconFontSwing.buildIcon(FontAwesome.COGS, 12));
		tabbedPane.setEnabledAt(2, false);
        tabbedPane.setEnabledAt(3, false);
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
			if (path.isEmpty()) return;

			doInBackground("Loading ontology...", () -> {
                mapper.setModel(null);
                setTitle("PhenoMan-Editor");
                try {
                    mapper.setModel(new PhenotypeManager(path, false));
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                        new JFrame(),
                        String.format("Could not load ontology from file %s!\nReason: %s", path, e.getLocalizedMessage())
                    );
                }
                setTitle("PhenoMan-Editor - " + path);
                phenotypeForm.setMapper(mapper);
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setEnabledAt(3, true);
                tabbedPane.setSelectedIndex(2);
                reloadEntityTree();
                return null;
			});
		});
		reloadButton.setIcon(IconFontSwing.buildIcon(FontAwesome.REFRESH, 12));
        reloadButton.addActionListener(actionEvent -> reloadEntityTree());
        treeSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) return;
                if (treeSearchField.getText().isEmpty()) return;

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

        mapper = new PhenotypeManagerMapper();
    }

    private MutableTreeNode convertToTreeNode(EntityTreeNode entityNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entityNode.getEntity(), true);
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

    @SuppressWarnings("SameParameterValue")
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

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
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
        tree = new PhenotypeTree(this);
        tree.setModel(new DefaultTreeModel(null));
        tree.setShowsRootHandles(true);
        phenotypeForm = new PhenotypeForm(this);
    }

    private void reloadEntityTree() {
        if (mapper == null || !mapper.hasModel()) return;
        EntityTreeNode node = mapper.getModel().getReader().getEntityTreeWithPhenotypes(false);
        tree.setModel(new DefaultTreeModel(convertToTreeNode(node)));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Entity entity = null;

        if (ae.getSource() instanceof DefaultMutableTreeNode) {
            entity = (Entity) ((DefaultMutableTreeNode) ae.getSource()).getUserObject();
        }

        PhenotypeBean phenotype;
        switch (ae.getActionCommand()) {
            case "inspect":
                if (entity == null) return;
                phenotype = mapper.loadEntity(entity);
                break;
            case "add_category":
                phenotype = new PhenotypeBean();
                phenotype.setType(EntityType.CATEGORY);
                if (entity != null) // this is dirty and should be replaced by a variable access to COP class
                    phenotype.setSuperCategories(Collections.singletonList(entity.asCategory().getName()));
                break;
            case "add_abstract_single_phenotype":
                phenotype = new PhenotypeBean();
                phenotype.setType(EntityType.ABSTRACT_SINGLE_PHENOTYPE);
                if (entity != null)
                    phenotype.setSuperCategories(Collections.singletonList(entity.asCategory().getName()));
                break;
            case "add_abstract_calculation_phenotype":
                phenotype = new PhenotypeBean();
                phenotype.setType(EntityType.ABSTRACT_CALCULATION_PHENOTYPE);
                if (entity != null)
                    phenotype.setSuperCategories(Collections.singletonList(entity.asCategory().getName()));
                break;
            case "add_abstract_boolean_phenotype":
                phenotype = new PhenotypeBean();
                phenotype.setType(EntityType.ABSTRACT_BOOLEAN_PHENOTYPE);
                if (entity != null)
                    phenotype.setSuperCategories(Collections.singletonList(entity.asCategory().getName()));
                break;
            case "add_restricted_phenotype":
                if (entity == null) return;
                phenotype = new PhenotypeBean();
                if (entity.isAbstractSinglePhenotype()) {
                    phenotype.setType(EntityType.RESTRICTED_SINGLE_PHENOTYPE);
                } else if (entity.isAbstractCalculationPhenotype()) {
                    phenotype.setType(EntityType.RESTRICTED_CALCULATION_PHENOTYPE);
                } else {
                    phenotype.setType(EntityType.RESTRICTED_BOOLEAN_PHENOTYPE);
                }
                phenotype.setSuperPhenotype(entity.getName());
                break;
            case "delete":
                if (entity == null) return;
                PhenotypeManager model = mapper.getModel();
                model.getCleaner().removeEntities(entity.getName());
                model.getWriter().write();
                reloadEntityTree();
            case "phenotype_saved":
                reloadEntityTree();
            default:
                return;
        }

        phenotypeForm.setData(phenotype);
        phenotypeForm.setVisible(true);
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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane = new JTabbedPane();
        tabbedPane.setEnabled(true);
        tabbedPane.setVisible(true);
        contentPane.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Introduction", panel1);
        introductionScrollPane = new JScrollPane();
        panel1.add(introductionScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 4, new Insets(5, 5, 5, 5), -1, -1));
        introductionScrollPane.setViewportView(panel2);
        final JLabel label1 = new JLabel();
        label1.setText("<html>\n  <div>\n    <h2>Introduction</h2>\n\n    <p style=\"text-align: justify\">\n      We consider a phenotype as an individual (as in <a href=\"http://www.onto-med.de/ontologies/gfo/\">General Formal Ontology</a>, GFO),\n      for example, the weight of a concrete person. Abstract instantiable entities, that are instantiated by phenotypes,\n      are called phenotype classes (e.g., the abstract property ‘body length’ possess individual length values as instances.\n\n      There are single and composite properties (traits), and correspondingly, single and composite phenotypes:\n    </p>\n\n    <ul>\n      <li>Single phenotype: a single property (e.g., age, weight, height)</li>\n      <li>Composite phenotype: a composite property that consists of single properties (e.g.,\n        <a href=\"https://en.wikipedia.org/wiki/Body_mass_index\">BMI</a>,\n        <a href=\"https://doi.org/10.1007/BF01709751\">SOFA Score</a>) of an organism or its subsystem\n      </li>\n      <li>Boolean phenotype: a Boolean expression based on has_part relations</li>\n      <li>Mathematical phenotype: a calculation rule (e.g., BMI = weight / height²)</li>\n      <li>Furthermore, composite phenotype classes can associate certain conditions with specific predefined\n        values (scores). Such phenotype classes we call score phenotype classes.\n      </li>\n    </ul>\n\n    <p style=\"text-align: justify\">\n      We distinguish between restricted and non-restricted phenotype classes, depending on whether their extensions\n      (set of instances) are restricted to a certain range of individual phenotypes by defined conditions or all\n      instances are allowed. For example:\n    </p>\n\n    <ul>\n      <li>Non-restricted: phenotype class “age”, which is instantiated by the ages of all living beings</li>\n      <li>Restricted: phenotype class “young age”, which is instantiated by the ages of the young ones (age below some pleasant value).</li>\n    </ul>\n\n    <h2>Architecture of the Core Ontology of Phenotypes</h2>\n\n    <p style=\"text-align: justify\">\n      The Core Ontology of Phenotypes (COP) enables ontologists to model phenotype classes, so that phenotypes can be\n      classified in phenotype classes based on instance data sets (e.g., of a patient). We used the class gfo:Property\n      of the GFO to model properties or traits and we defined the class cop:Phenotype as subclass of gfo:Property\n      (Figure 1 (a)). According to our definitions in Section 1, there are six types of phenotype classes:\n    </p>\n\n    <ul>\n      <li>non-restricted (NSiP) and restricted (RSiP) single phenotype classes</li>\n      <li>non-restricted (NScP) and restricted (RScP) score phenotype classes</li>\n      <li>non-restricted (NMaP) and restricted (RMaP) mathematical phenotype classes</li>\n    </ul>\n\n    <p style=\"text-align: justify\">\n      Each subclass of cop:Single_Phenotype, cop:Score_Phenotype and cop:Mathematical_Phenotype is a phenotype class\n      and is instantiated by phenotypes. Direct subclasses are non-restricted, while subclasses of non-restricted\n      phenotype classes are restricted (e.g., age greater than or equal to 20 years: Age_ge_20).\n    </p>\n  </div>\n</html>");
        panel2.add(label1, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(800, -1), new Dimension(800, -1), 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("<html>\n  <p style=\"text-align: justify\">\n    Phenotype classes possess various common attributes (e.g., labels, descriptions and links to external concepts).\n    Additional attributes vary depending on the phenotype class. NSiP classes define the datatype and a unit of\n    measure, NMaP classes have a mathematical formula, RSiP and RMaP classes have restrictions and RScP classes have\n    a Boolean expression and an optional score. Logical relations between phenotype classes as well as range\n    restrictions are represented by anonymous equivalent classes or general class axioms based on property\n    restrictions.\n  </p>\n</html>");
        panel2.add(label2, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(800, -1), new Dimension(800, -1), 0, false));
        exampleImage = new JLabel();
        exampleImage.setText("");
        panel2.add(exampleImage, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copImage = new JLabel();
        copImage.setText("");
        panel2.add(copImage, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 4, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane.addTab("Ontology selection", panel3);
        final JLabel label3     = new JLabel();
        Font         label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Please select a phenotype ontology, you want to work on.");
        panel3.add(label3, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Ontology:");
        panel3.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ontologyPath = new JTextField();
        ontologyPath.setText("");
        panel3.add(ontologyPath, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseButton = new JButton();
        browseButton.setText("Browse");
        panel3.add(browseButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadOntologyButton = new JButton();
        loadOntologyButton.setEnabled(true);
        loadOntologyButton.setText("Load Ontology");
        panel3.add(loadOntologyButton, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel3.add(spacer5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setEnabled(true);
        tabbedPane.addTab("Editor", panel4);
        final JSplitPane splitPane1 = new JSplitPane();
        panel4.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel5);
        reloadButton = new JButton();
        reloadButton.setText("Reload");
        panel5.add(reloadButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tree.setDragEnabled(true);
        panel5.add(tree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, -1), null, null, 0, false));
        treeSearchField = new JTextField();
        treeSearchField.setToolTipText("Search for phenotype tree node");
        panel5.add(treeSearchField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel6);
        phenotypeFormScrollPane = new JScrollPane();
        panel6.add(phenotypeFormScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        phenotypeFormScrollPane.setViewportView(phenotypeForm.$$$getRootComponent$$$());
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("Execute query", panel7);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane.addTab("Settings", panel8);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
