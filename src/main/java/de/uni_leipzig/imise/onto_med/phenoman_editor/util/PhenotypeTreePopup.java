package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PhenotypeTreePopup extends JPopupMenu {
    public PhenotypeTreePopup(ActionListener listener) {
        IconFontSwing.register(FontAwesome.getIconFont());

        JMenuItem inspectItem = new JMenuItem("Inspect", IconFontSwing.buildIcon(FontAwesome.SEARCH, 12));
        inspectItem.addActionListener(listener);
        inspectItem.setActionCommand("inspect");

        JMenuItem addCategoryItem = new JMenuItem("Add category", IconFontSwing.buildIcon(FontAwesome.PLUS, 12));
        addCategoryItem.setActionCommand("add_category");
        addCategoryItem.addActionListener(listener);

        add(inspectItem);
        add(new JSeparator());
        add(addCategoryItem);
    }
}
