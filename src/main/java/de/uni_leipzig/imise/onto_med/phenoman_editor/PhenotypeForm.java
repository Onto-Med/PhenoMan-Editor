package de.uni_leipzig.imise.onto_med.phenoman_editor;

import javax.swing.*;

public class PhenotypeForm extends JPanel {
    private JTextField textField1;
    private JTextField textField2;
    private JTextArea textArea1;
    private JPanel contentPane;
    private JButton saveButton;

    public void setData(Phenotype data) {
        textField1.setText(data.getId());
        textField2.setText(data.getLabel());
        textArea1.setText(data.getDefinition());
    }

    public void getData(Phenotype data) {
        data.setId(textField1.getText());
        data.setLabel(textField2.getText());
        data.setDefinition(textArea1.getText());
    }

    public boolean isModified(Phenotype data) {
        if (textField1.getText() != null ? !textField1.getText().equals(data.getId()) : data.getId() != null)
            return true;
        if (textField2.getText() != null ? !textField2.getText().equals(data.getLabel()) : data.getLabel() != null)
            return true;
        if (textArea1.getText() != null ? !textArea1.getText().equals(data.getDefinition()) : data.getDefinition() != null)
            return true;
        return false;
    }
}
