package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;
import org.smith.phenoman.model.phenotype.top_level.Entity;
import org.smith.phenoman.model.phenotype.top_level.Phenotype;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class PhenotypeTreeCellRenderer extends DefaultTreeCellRenderer {

    public PhenotypeTreeCellRenderer() {
        super();
        IconFontSwing.register(FontAwesome.getIconFont());
    }

    @Override
    public Component getTreeCellRendererComponent(JTree jTree, Object o, boolean b, boolean b1, boolean b2, int i, boolean b3) {
        super.getTreeCellRendererComponent(jTree, o, b, b1, b2, i, b3);
        Object obj = ((DefaultMutableTreeNode) o).getUserObject();

        if (obj instanceof Entity) {
            setLabelFromEntity((Entity) obj);
        } else {
            setText("Phenotype Category");
        }

        return this;
    }

    private void setLabelFromEntity(Entity entity) {
        if (entity.getMainTitleText().isBlank()) {
            setText(entity.getName());
        } else {
            setText(entity.getMainTitleText());
        }
        FontAwesome fontAwesome;
        Color color;

        if (entity.isCategory()) {
            fontAwesome = FontAwesome.FOLDER_OPEN;
            color = new Color(108, 117, 125);
        } else {
            if (entity.isAbstractPhenotype()) {
                color = new Color(0, 123, 255);
            } else {
                color = new Color(255, 193, 7);
            }

            if (entity.isAbstractBooleanPhenotype() || entity.isRestrictedBooleanPhenotype()) {
                fontAwesome = FontAwesome.CHECK_CIRCLE;
            } else if (entity.isAbstractCalculationPhenotype() || entity.isRestrictedCalculationPhenotype()){
                fontAwesome = FontAwesome.CALCULATOR;
            } else {
                fontAwesome = FontAwesome.CALENDAR_O;
            }
        }

        setIcon(IconFontSwing.buildIcon(fontAwesome, 12, color));
    }
}
