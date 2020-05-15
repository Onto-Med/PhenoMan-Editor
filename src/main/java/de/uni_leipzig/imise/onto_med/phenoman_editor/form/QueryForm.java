package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QueryForm extends PhenotypeTab {
    private JPanel contentPane;
    private PhenotypeTreeForm tree;

    public QueryForm() {
        super();
        $$$setupUI$$$();
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "reload":
                reloadEntityTree();
            default:
        }
    }

    private void createUIComponents() {
        tree = new PhenotypeTreeForm(this);
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
        splitPane1.setLeftComponent(tree.$$$getRootComponent$$$());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
