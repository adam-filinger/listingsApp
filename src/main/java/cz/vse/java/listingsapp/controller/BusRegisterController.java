package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the business registration view.
 * @author Adam Filinger
 * @version 1.1
 */
public class BusRegisterController implements Initializable {

    @FXML
    private VBox formContainer;

    private final BusinessForm businessForm = new BusinessForm();
    private Form form;
    private UserService userService;
    private Uzivatel user;

    /**
     * Sets the user for the controller.
     * @param user The user.
     */
    public void setUser(Uzivatel user) {
        this.user = user;
    }

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userService = new UserService();

        StringField icoField = Field.ofStringType(businessForm.icoProperty())
                .label("ICO")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> Pattern.matches("^\\d{8}$", s),
                        "Please enter a valid ICO (8 digits)"));

        StringField nameField = Field.ofStringType(businessForm.nameProperty())
                .label("Company Name")
                .required("This field is required");

        StringField emailField = Field.ofStringType(businessForm.emailProperty())
                .label("Company Email")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> Pattern.matches("^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$", s),
                        "Please enter a valid email address"));

        StringField telField = Field.ofStringType(businessForm.telProperty())
                .label("Telephone")
                .required("This field is required")
                .validate(CustomValidator.forPredicate(
                        s -> Pattern.matches("^(\\+420)? ?\\d{3} ?\\d{3} ?\\d{3}$", s),
                        "Please enter a valid telephone number"));

        form = Form.of(
                Group.of(
                        icoField,
                        nameField,
                        emailField,
                        telField
                )
        ).title("Register as Business");

        formContainer.getChildren().add(new FormRenderer(form));
    }

    /**
     * Handles the register button action.
     */
    @FXML
    private void register() {
        form.persist();

        if (!form.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        PravnickaOsoba pravnickaOsoba = new PravnickaOsoba();
        pravnickaOsoba.setIco(businessForm.icoProperty().get());
        pravnickaOsoba.setName(businessForm.nameProperty().get());
        pravnickaOsoba.setEmail(businessForm.emailProperty().get());
        pravnickaOsoba.setTel(businessForm.telProperty().get());
        pravnickaOsoba.setUzivatel(user);

        if (userService.saveBusiness(pravnickaOsoba)) {
            showSuccessDialog();
        } else {
            showErrorDialog();
        }
    }

    /**
     * Handles the go to main menu button action.
     */
    @FXML
    private void goToMainMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            MainViewController mainViewController = fxmlLoader.getController();
            mainViewController.setUser(user, userService.isBusiness(user));
            Stage stage = (Stage) formContainer.getScene().getWindow();
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
     * Shows a success dialog.
     */
    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Business registered successfully!");
        alert.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            alert.close();
            goToMainMenu();
        });
        delay.play();
    }

    /**
     * Shows an error dialog.
     */
    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred during registration.");
        alert.setContentText("An entity with this ICO or email already exists.");

        ButtonType backButton = new ButtonType("Back to Registration");
        ButtonType mainMenuButton = new ButtonType("Back to Main Menu");

        alert.getButtonTypes().setAll(backButton, mainMenuButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == mainMenuButton) {
            goToMainMenu();
        }
    }
}
