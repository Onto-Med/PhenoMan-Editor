package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import org.smith.phenoman.model.phenotype.top_level.Entity;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PhenotypeTree extends JTree implements ActionListener {
    ChangeListener listener;

    public PhenotypeTree(ChangeListener listener) {
        super();
        this.listener = listener;
        setCellRenderer(new PhenotypeTreeCellRenderer());
        JPopupMenu popup = new PhenotypeTreePopup(this);

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                TreePath selectedPath = getPathForLocation(e.getX(), e.getY());
                setSelectionPath(selectedPath); // update selection to clicked node
                if (e.isPopupTrigger())
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        DefaultMutableTreeNode node;
        TreePath path = this.getSelectionPath();
        if (path == null) return;

        node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node == null || node.getUserObject() == null || !(node.getUserObject() instanceof Entity)) return;

        switch (ae.getActionCommand()) {
            case "inspect":
                listener.stateChanged(new ChangeEvent(node));
                break;
            case "add_category":
                System.out.println("add_category triggered");
                break;
            case "add_abstract_phenotype":
                System.out.println("add_abstract_phenotype triggered");
                break;
            case "add_restricted_phenotype":
                System.out.println("add_restricted_phenotype triggered");
                break;
        }
    }
}
