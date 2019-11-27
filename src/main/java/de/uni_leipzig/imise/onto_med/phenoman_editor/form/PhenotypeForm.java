package de.uni_leipzig.imise.onto_med.phenoman_editor.form;

import de.uni_leipzig.imise.onto_med.phenoman_editor.bean.PhenotypeBean;
import de.uni_leipzig.imise.onto_med.phenoman_editor.field.LocalizedStringField;

import javax.swing.*;

public class PhenotypeForm extends JPanel {
    private JTextField idField;
    private JPanel contentPane;
    private JButton saveButton;
    private LocalizedStringField descriptionsField;
    private LocalizedStringField titlesField;
    private JTextField mainTitleField;
    private LocalizedStringField synonymsField;
    private JTextField ucumField;
    private JLabel superPhenotypeField;

    public PhenotypeForm() {

    }

    public void setData(PhenotypeBean data) {
        idField.setText(data.getId());
        mainTitleField.setText(data.getMainTitle());
        titlesField.setData(data.getTitles());
        synonymsField.setData(data.getSynonyms());
        descriptionsField.setData(data.getDescriptions());
        ucumField.setText(data.getUcum());

        if (data.getSuperPhenotype() != null) superPhenotypeField.setText(data.getSuperPhenotype().getMainTitleText());
    }

    public void getData(PhenotypeBean data) {
        data.setId(idField.getText());
        data.setMainTitle(mainTitleField.getText());
        data.setTitles(titlesField.getData());
        data.setSynonyms(synonymsField.getData());
        data.setDescriptions(descriptionsField.getData());
        data.setUcum(ucumField.getText());
    }

    public boolean isModified(PhenotypeBean data) {
        if (idField.getText() != null ? !idField.getText().equals(data.getId()) : data.getId() != null)
            return true;
        if (mainTitleField.getText() != null ? !mainTitleField.getText().equals(data.getMainTitle()) : data.getMainTitle() != null)
            return true;
        if (titlesField.isModified(data.getTitles()))
            return true;
        if (synonymsField.isModified(data.getSynonyms()))
            return true;
        if (descriptionsField.isModified(data.getDescriptions()))
            return true;
        if (ucumField.getText() != null ? !ucumField.getText().equals(data.getUcum()) : data.getUcum() != null)
            return true;
        return false;
    }
}
