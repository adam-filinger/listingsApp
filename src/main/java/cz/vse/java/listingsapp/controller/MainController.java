package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.structure.PasswordField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the main view of the application.
 * Handles user registration.
 * @author Adam Filinger
 * @version 1.0
 */
public class MainController implements Initializable {

    @FXML
    public BorderPane rootPane;

    private final UserForm userForm = new UserForm();
    private Form signUpForm;
    private UserService userService;

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();

        StringField nameField = Field.ofStringType(userForm.nameProperty())
                .label("Full Name")
                .required("This field is required");

        StringField usernameField = Field.ofStringType(userForm.usernameProperty())
                .label("Username")
                .required("This field is required");

        StringField emailField = Field.ofStringType(userForm.emailProperty())
                .label("Email")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> Pattern.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$", s),
                        "Please enter a valid email address"));

        PasswordField passwordField = Field.ofPasswordType(userForm.passwordProperty())
                .label("Password")
                .required("This field is required");

        PasswordField confirmPasswordField = Field.ofPasswordType(userForm.confirmPasswordProperty())
                .label("Confirm Password")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> s.equals(passwordField.getValue()),
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

    /**
     * Handles the sign up button action.
     * Validates the form and saves the new user to the database.
     */
    @FXML
    private void signUp() {
        signUpForm.persist();

        if (!signUpForm.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        Uzivatel user = new Uzivatel();
        user.setName(userForm.nameProperty().get());
        user.setUsername(userForm.usernameProperty().get());
        user.setEmail(userForm.emailProperty().get());
        user.setPasswd(userForm.passwordProperty().get());

        if (userService.userExists(user.getUsername(), user.getEmail())) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "A user with this username or email already exists.");
            return;
        }

        if (userService.saveUser(user)) {
            showSuccessDialog();
            signUpForm.reset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving the user.");
        }
    }

    /**
     * Handles the go to login button action.
     * Switches the scene to the login view.
     */
    @FXML
    private void goToLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows an alert dialog.
     * @param alertType The type of the alert.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a success dialog after successful registration.
     */
    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("User registered successfully!");

        ButtonType goToLoginButton = new ButtonType("Go to Login");
        ButtonType closeButton = new ButtonType("Close");

        alert.getButtonTypes().setAll(goToLoginButton, closeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == goToLoginButton) {
            goToLogin();
        }
    }
}
