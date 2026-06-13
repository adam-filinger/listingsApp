package cz.vse.java.listingsapp.controller.login;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.structure.PasswordField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.controller.Controller;
import cz.vse.java.listingsapp.controller.MainViewController;
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

/**
 * Controller for the login view of the application.
 * Handles user login.
 * @author Adam Filinger
 * @version 1.1
 */
public class LoginController extends Controller implements Initializable {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    public BorderPane rootPane;

    private final LoginForm loginForm = new LoginForm();
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

        StringField identifierField = Field.ofStringType(loginForm.identifierProperty())
                .label("Username or Email")
                .required("This field is required");

        PasswordField passwordField = Field.ofPasswordType(loginForm.passwordProperty())
                .label("Password")
                .required("This field is required");

        form = Form.of(
                Group.of(
                        identifierField,
                        passwordField
                )
        ).title("Login");

        rootPane.setCenter(new FormRenderer(form));
    }

    /**
     * Handles the login button action.
     * Validates the form and logs in the user.
     */
    @FXML
    private void login() {
        form.persist();

        if (!form.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        Uzivatel user = userService.login(loginForm.identifierProperty().get(), loginForm.passwordProperty().get());

        if (user != null) {
            boolean isBusiness = userService.isBusiness(user);
            // Switch to the main view
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/main-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                MainViewController mainViewController = fxmlLoader.getController();
                mainViewController.setUser(user);
                mainViewController.onView();
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.setScene(scene);
            }
            catch (IOException e) {
                logger.warn("Failed to load main view, returning to login view", e);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/email or password.");
        }
    }

    /**
     * Handles the go to sign up button action.
     * Switches the scene to the sign up view.
     */
    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/singup-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            logger.warn("Failed to load sign up view, returning to login view", e);
        }
    }

}
