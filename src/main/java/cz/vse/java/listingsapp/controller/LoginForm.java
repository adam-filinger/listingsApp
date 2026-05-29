package cz.vse.java.listingsapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginForm {
    private final StringProperty identifier = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");

    public StringProperty identifierProperty() {
        return identifier;
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
