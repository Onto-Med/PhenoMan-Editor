package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import care.smith.phep.phenoman.core.model.phenotype.top_level.Entity;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionListener;

public class PhenotypeTreePopup extends JPopupMenu {


    private final JMenuItem inspectItem;
    private final JSeparator inspectSeparator;
    private final JMenuItem addCategoryItem;
    private final JMenuItem addAbstractSinglePhenotypeItem;
    private final JMenuItem addAbstractCalculationPhenotypeItem;
    private final JMenuItem addAbstractBooleanPhenotypeItem;
    private final JMenuItem addRestrictedPhenotypeItem;
    private final JMenuItem importFromArtDecorItem;
    private final JSeparator deleteSeparator;
    private final JMenuItem deleteItem;


    public PhenotypeTreePopup(ActionListener listener) {
        IconFontSwing.register(FontAwesome.getIconFont());

        inspectItem = new JMenuItem("Inspect", IconFontSwing.buildIcon(FontAwesome.SEARCH, 12));
        inspectItem.addActionListener(listener);
        inspectItem.setActionCommand("inspect");

        inspectSeparator = new JSeparator();

        addCategoryItem = new JMenuItem("Add category", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(108, 117, 125)));
        addCategoryItem.setActionCommand("add_category");
        addCategoryItem.addActionListener(listener);

        addAbstractSinglePhenotypeItem = new JMenuItem("Add single phenotype", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(0, 123, 255)));
        addAbstractSinglePhenotypeItem.setActionCommand("add_abstract_single_phenotype");
        addAbstractSinglePhenotypeItem.addActionListener(listener);

        addAbstractCalculationPhenotypeItem = new JMenuItem("Add calculated phenotype", IconFontSwing.buildIcon(FontAwesome.PLUS_SQUARE, 12, new Color(0, 123, 255)));
        addAbstractCalculationPhenotypeItem.setActionCommand("add_abstract_calculation_phenotype");
        addAbstractCalculationPhenotypeItem.addActionListener(listener);

        addAbstractBooleanPhenotypeItem = new JMenuItem("Add boolean phenotype", IconFontSwing.buildIcon(FontAwesome.PLUS_CIRCLE, 12, new Color(0, 123, 255)));
        addAbstractBooleanPhenotypeItem.setActionCommand("add_abstract_boolean_phenotype");
        addAbstractBooleanPhenotypeItem.addActionListener(listener);

        addRestrictedPhenotypeItem = new JMenuItem("Add restriction", IconFontSwing.buildIcon(FontAwesome.PLUS, 12, new Color(255, 193, 7)));
        addRestrictedPhenotypeItem.setActionCommand("add_restricted_phenotype");
        addRestrictedPhenotypeItem.addActionListener(listener);

        deleteSeparator = new JSeparator();

        importFromArtDecorItem = new JMenuItem("Import from ART DECOR", IconFontSwing.buildIcon(FontAwesome.DOWNLOAD, 12));
        importFromArtDecorItem.setActionCommand("import_from_art_decor");
        importFromArtDecorItem.addActionListener(listener);

        deleteItem = new JMenuItem("Delete", IconFontSwing.buildIcon(FontAwesome.TRASH_O, 12, new Color(220, 53, 69)));
        deleteItem.addActionListener(listener);
        deleteItem.setActionCommand("delete");

        add(inspectItem);
        add(inspectSeparator);
        add(addCategoryItem);
        add(addAbstractSinglePhenotypeItem);
        add(addAbstractCalculationPhenotypeItem);
        add(addAbstractBooleanPhenotypeItem);
        add(addRestrictedPhenotypeItem);
        add(deleteSeparator);
        add(importFromArtDecorItem);
        add(deleteItem);
    }

    @Override
    public void show(Component c, int x, int y) {
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) ((PhenotypeTree) c).getLastSelectedPathComponent());
        if (node == null) return;

        Entity entity = (Entity) node.getUserObject();

        setInspectVisible(entity != null);
        addCategoryItem.setVisible(entity == null || entity.isCategory());
        setAbstractVisible(entity == null || entity.isCategory());
        addRestrictedPhenotypeItem.setVisible(entity != null && entity.isAbstractPhenotype());
        importFromArtDecorItem.setVisible(entity == null);
        deleteItem.setVisible(entity != null);

        super.show(c, x, y);
    }

    private void setInspectVisible(boolean visible) {
        inspectItem.setVisible(visible);
        inspectSeparator.setVisible(visible);
    }

    private void setAbstractVisible(boolean visible) {
        addAbstractSinglePhenotypeItem.setVisible(visible);
        addAbstractCalculationPhenotypeItem.setVisible(visible);
        addAbstractBooleanPhenotypeItem.setVisible(visible);
    }
}
