package cz.vse.java.listingsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the form for logging in a user.
 * @author Adam Filinger
 * @version 1.0
 */
public class LoginForm {
    private final StringProperty identifier = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");

    /**
     * @return the identifier property
     */
    public StringProperty identifierProperty() {
        return identifier;
    }

    /**
     * @return the password property
     */
    public StringProperty passwordProperty() {
        return password;
    }
}
