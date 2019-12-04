package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import org.smith.phenoman.model.phenotype.top_level.Entity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhenotypeTree extends JTree implements ActionListener {
    ActionListener listener;

    public PhenotypeTree(ActionListener listener) {
        super();
        this.listener = listener;
        setCellRenderer(new PhenotypeTreeCellRenderer());
        JPopupMenu popup = new PhenotypeTreePopup(this);

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                TreePath selectedPath = getPathForLocation(e.getX(), e.getY());
                setSelectionPath(selectedPath);
                if (e.isPopupTrigger())
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY()); // TODO: overwrite show() to set visibility of menu items
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        DefaultMutableTreeNode node;
        TreePath path = this.getSelectionPath();
        if (path == null) return;

        node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node == null || node.getUserObject() == null || !(node.getUserObject() instanceof Entity)) return;

        listener.actionPerformed(new ActionEvent(node, ActionEvent.ACTION_PERFORMED, ae.getActionCommand()));
    }
}
