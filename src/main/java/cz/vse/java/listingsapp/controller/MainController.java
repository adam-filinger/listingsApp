package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.structure.PasswordField; // Import the correct class
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    @FXML
    public BorderPane rootPane;

    private final Uzivatel userModel = new Uzivatel();
    private Form signUpForm;
    private final UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringField nameField = Field.ofStringType(userModel.nameProperty())
                .label("Full Name")
                .required("This field is required");

        StringField usernameField = Field.ofStringType(userModel.usernameProperty())
                .label("Username")
                .required("This field is required");

        StringField emailField = Field.ofStringType(userModel.emailProperty())
                .label("Email")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> Pattern.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$", s),
                        "Please enter a valid email address"));

        // Use the correct PasswordField type
        PasswordField passwordField = Field.ofPasswordType(userModel.passwdProperty())
                .label("Password")
                .required("This field is required");

        // Use the correct PasswordField type
        PasswordField confirmPasswordField = Field.ofPasswordType(userModel.confirmPasswdProperty())
                .label("Confirm Password")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> s.equals(passwordField.getValue()), // Validate against the other field's value
                        "Passwords do not match"));

        passwordField.valueProperty().addListener((obs, oldVal, newVal) -> confirmPasswordField.validate());

        signUpForm = Form.of(
                Group.of(
                        nameField,
                        usernameField,
                        emailField,
                        passwordField,
                        confirmPasswordField
                )
        ).title("Sign Up");

        rootPane.setCenter(new FormRenderer(signUpForm));
    }

    @FXML
    private void signUp() {
        signUpForm.persist();

        if (!signUpForm.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        if (userService.userExists(userModel.getUsername(), userModel.getEmail())) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "A user with this username or email already exists.");
            return;
        }

        if (userService.saveUser(userModel)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");
            signUpForm.reset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving the user.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
