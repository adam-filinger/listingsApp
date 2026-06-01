package cz.vse.java.listingsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the form for creating a new user.
 * @author Adam Filinger
 * @version 1.0
 */
public class UserForm {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty confirmPassword = new SimpleStringProperty("");

    /**
     * @return the name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * @return the username property
     */
    public StringProperty usernameProperty() {
        return username;
    }

    /**
     * @return the email property
     */
    public StringProperty emailProperty() {
        return email;
    }

    /**
     * @return the password property
     */
    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * @return the confirm password property
     */
    public StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }
}
