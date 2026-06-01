package cz.vse.java.listingsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the form for registering a new business.
 * @author Adam Filinger
 * @version 1.0
 */
public class BusinessForm {
    private final StringProperty ico = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty tel = new SimpleStringProperty("");

    /**
     * @return the ICO property
     */
    public StringProperty icoProperty() {
        return ico;
    }

    /**
     * @return the name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * @return the email property
     */
    public StringProperty emailProperty() {
        return email;
    }

    /**
     * @return the telephone property
     */
    public StringProperty telProperty() {
        return tel;
    }
}
