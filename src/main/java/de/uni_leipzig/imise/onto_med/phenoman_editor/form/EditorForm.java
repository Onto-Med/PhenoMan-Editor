package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.EntityType;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeManagerMapper;
import org.smith.phenoman.man.PhenotypeManager;
import org.smith.phenoman.model.phenotype.top_level.Entity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class EditorForm extends PhenotypeTab {
    private JPanel            contentPane;
    private JScrollPane       formScrollPane;
    private PhenotypeForm     form;
    private PhenotypeTreeForm tree;


    public EditorForm() {
        super();
        $$$setupUI$$$();
        form.setVisible(false);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    }

    @Override
    public void setMapper(PhenotypeManagerMapper mapper) {
        this.mapper = mapper;
        form.setMapper(mapper);
    }

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
                model.removeEntities(entity.getName());
                model.write();
                reloadEntityTree();
            case "reload":
            case "phenotype_saved":
                reloadEntityTree();
            default:
                return;
        }

        form.setData(phenotype);
        form.setVisible(true);
    }

    private void createUIComponents() {
        tree = new PhenotypeTreeForm(this);
        form = new PhenotypeForm(this);
    }

    private void reloadEntityTree() {
        if (mapper == null || !mapper.hasModel()) return;
        tree.fillTree(mapper.getModel().getEntityTreeWithPhenotypes(false));
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
        final JSplitPane splitPane1 = new JSplitPane();
        contentPane.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel1);
        panel1.add(tree.$$$getRootComponent$$$(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel2);
        formScrollPane = new JScrollPane();
        panel2.add(formScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        formScrollPane.setViewportView(form.$$$getRootComponent$$$());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
