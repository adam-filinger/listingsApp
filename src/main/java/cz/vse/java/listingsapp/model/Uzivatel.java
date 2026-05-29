package cz.vse.java.listingsapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Uzivatel {
    // No need for an ID property for a new user
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty passwd = new SimpleStringProperty("");
    private final StringProperty confirmPasswd = new SimpleStringProperty(""); // For form validation only

    // --- Property Accessors ---

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty passwdProperty() {
        return passwd;
    }

    public StringProperty confirmPasswdProperty() {
        return confirmPasswd;
    }

    // --- Standard Getters/Setters for convenience ---

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPasswd() {
        return passwd.get();
    }

    public void setPasswd(String passwd) {
        this.passwd.set(passwd);
    }
}
