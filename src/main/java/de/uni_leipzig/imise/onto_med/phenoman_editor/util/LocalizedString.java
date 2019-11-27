package de.uni_leipzig.imise.onto_med.phenoman_editor.util;

import java.util.Locale;

public class LocalizedString {
    private String string;
    private Locale language;

    public LocalizedString(String string, Locale language) {
        this.string = string;
        this.language = language;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Locale getLocale() {
        return language;
    }

    public void setLocale(Locale language) {
        this.language = language;
    }
}
