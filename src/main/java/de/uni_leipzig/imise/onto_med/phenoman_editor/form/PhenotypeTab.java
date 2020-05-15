package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import de.uni_leipzig.imise.onto_med.phenoman_editor.util.PhenotypeManagerMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class PhenotypeTab extends JPanel implements ActionListener {
    protected PhenotypeManagerMapper mapper;

    public PhenotypeTab() {
        super();
    }

    public void setMapper(PhenotypeManagerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}
