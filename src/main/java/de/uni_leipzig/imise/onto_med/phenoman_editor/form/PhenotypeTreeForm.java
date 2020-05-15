package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeTree;
import de.uni_leipzig.imise.onto_med.phenoman_editor.util.TreeTransferHandler;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.smith.phenoman.model.category_tree.EntityTreeNode;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

public class PhenotypeTreeForm extends JPanel implements ActionListener {
    private final ActionListener listener;
    private       JTextField     treeSearchField;
    private       PhenotypeTree  tree;
    private       JPanel         contentPane;
    private       JButton        reloadButton;

    /**
     * Create a new PhenotypeTreeForm with a JTree of phenotypes and a search field.
     *
     * @param listener A listener, which should handle click events of the JTree.
     */
    public PhenotypeTreeForm(ActionListener listener) {
        super();
        $$$setupUI$$$();
        this.listener = listener;
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

        reloadButton.setIcon(IconFontSwing.buildIcon(FontAwesome.REFRESH, 12));
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "reload"));
            }
        });
    }

    public PhenotypeTreeForm(ActionListener listener, TransferHandler transferHandler) {
        this(listener);
        if (transferHandler != null) tree.setTransferHandler(transferHandler);
    }

    public void fillTree(EntityTreeNode node) {
        tree.setModel(new DefaultTreeModel(convertToTreeNode(node)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(e);
    }

    private void createUIComponents() {
        tree = new PhenotypeTree(this);
        tree.setModel(new DefaultTreeModel(null));
        tree.setShowsRootHandles(true);
    }

    private MutableTreeNode convertToTreeNode(EntityTreeNode entityNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(entityNode.getEntity(), true);
        entityNode.getChildren().forEach(c -> node.add(convertToTreeNode(c)));
        return node;
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        treeSearchField = new JTextField();
        treeSearchField.setText("");
        treeSearchField.setToolTipText("Search for phenotype tree node");
        contentPane.add(treeSearchField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tree.setDragEnabled(true);
        contentPane.add(tree, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(100, -1), null, null, 0, false));
        reloadButton = new JButton();
        reloadButton.setText("Reload");
        contentPane.add(reloadButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
