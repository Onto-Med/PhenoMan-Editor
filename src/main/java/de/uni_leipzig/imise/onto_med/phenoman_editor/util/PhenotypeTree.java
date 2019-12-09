package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import javax.swing.*;
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
                    popup.show((JComponent) e.getSource(), e.getX(), e.getY());
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        TreePath path = this.getSelectionPath();
        if (path == null) return;

        listener.actionPerformed(new ActionEvent(path.getLastPathComponent(), ActionEvent.ACTION_PERFORMED, ae.getActionCommand()));
    }
}
