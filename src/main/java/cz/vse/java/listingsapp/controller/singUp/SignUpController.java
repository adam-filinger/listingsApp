package cz.vse.java.listingsapp.controller.singUp;

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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the sign-up view of the application.
 * Handles new user registration.
 * @author Adam Filinger
 * @version 1.2
 */
public class SignUpController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @FXML
    public BorderPane rootPane;

    private final UserForm userForm = new UserForm();
    private Form form;
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
                .label("Name")
                .required("This field is required");

        StringField usernameField = Field.ofStringType(userForm.usernameProperty())
                .label("Username")
                .required("This field is required");

        StringField emailField = Field.ofStringType(userForm.emailProperty())
                .label("Email")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                    s -> Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", s),
                    "Please enter a valid email address"
                ));

        PasswordField passwordField = Field.ofPasswordType(userForm.passwordProperty())
                .label("Password")
                .required("This field is required");

        PasswordField confirmPasswordField = Field.ofPasswordType(userForm.confirmPasswordProperty())
                .label("Confirm Password")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                    s -> s != null && s.equals(passwordField.valueProperty().get()),
                    "Passwords do not match"
                ));

        form = Form.of(
                Group.of(
                        nameField,
                        usernameField,
                        emailField,
                        passwordField,
                        confirmPasswordField
                )
        ).title("Sign Up");

        rootPane.setCenter(new FormRenderer(form));
    }

    /**
     * Handles the sign-up button action.
     * Validates the form and creates a new user.
     */
    @FXML
    private void signUp() {
        form.persist();

        if (!form.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        if (userService.userExists(userForm.usernameProperty().get(), userForm.emailProperty().get())) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "User with this username or email already exists.");
            return;
        }

        Uzivatel newUser = new Uzivatel();
        newUser.setName(userForm.nameProperty().get());
        newUser.setUsername(userForm.usernameProperty().get());
        newUser.setEmail(userForm.emailProperty().get());
        newUser.setPasswd(userForm.passwordProperty().get());

        if (userService.saveUser(newUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You can now log in.");
            goToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An unexpected error occurred.");
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
            logger.error("Failed to load login view.", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login view.");
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
}