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
        this.listener = listener;
        JPopupMenu popup = new PhenotypeTreePopup(this);

        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());
                }
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        DefaultMutableTreeNode node;

        TreePath path = this.getSelectionPath();
        node = (DefaultMutableTreeNode) path.getLastPathComponent();

        if (ae.getActionCommand().equals("inspect")) {
            listener.stateChanged(new ChangeEvent(node));
        } else if (ae.getActionCommand().equals("add_category")) {
            System.out.println("add_category triggered");
        }
    }
}
