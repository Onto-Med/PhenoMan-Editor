package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PhenotypeTreePopup extends JPopupMenu {
    public PhenotypeTreePopup(ActionListener listener) {
        IconFontSwing.register(FontAwesome.getIconFont());

        JMenuItem inspectItem = new JMenuItem("Inspect", IconFontSwing.buildIcon(FontAwesome.SEARCH, 12));
        inspectItem.addActionListener(listener);
        inspectItem.setActionCommand("inspect");

        JMenuItem addCategoryItem = new JMenuItem("Add category", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(108, 117, 125)));
        addCategoryItem.setActionCommand("add_category");
        addCategoryItem.addActionListener(listener);

        JMenuItem addAbstractPhenotypeItem = new JMenuItem("Add abstract phenotype", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(0, 123, 255)));
        addAbstractPhenotypeItem.setActionCommand("add_abstract_phenotype");
        addAbstractPhenotypeItem.addActionListener(listener);

        JMenuItem addRestrictedPhenotypeItem = new JMenuItem("Add restricted phenotype", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(255, 193, 7)));
        addRestrictedPhenotypeItem.setActionCommand("add_restricted_phenotype");
        addRestrictedPhenotypeItem.addActionListener(listener);

        JMenuItem deleteItem = new JMenuItem("Delete", IconFontSwing.buildIcon(FontAwesome.TRASH_O, 12, new Color(220, 53, 69)));
        deleteItem.addActionListener(listener);
        deleteItem.setActionCommand("delete");

        add(inspectItem);
        add(new JSeparator());
        add(addCategoryItem);
        add(addAbstractPhenotypeItem);
        add(addRestrictedPhenotypeItem);
        add(new JSeparator());
        add(deleteItem);
    }
}
