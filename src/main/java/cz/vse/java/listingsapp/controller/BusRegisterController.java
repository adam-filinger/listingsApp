package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.Poptavka;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the business registration view.
 * @author Adam Filinger
 * @version 1.2
 */
public class BusRegisterController extends Controller implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(BusRegisterController.class);

    @FXML
    private VBox formContainer;

    private final BusinessForm businessForm = new BusinessForm();
    private Form form;
    private UserService userService;
    private Uzivatel user;

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
    
    @Override
    void onView(Uzivatel user) {
        this.user = user;
    }

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

        try {
            userService.saveBusiness(pravnickaOsoba);
            showSuccessDialog();
        } catch (Exception e) {
            logger.error("Failed to register business", e);
            showErrorDialog();
        }
    }

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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
