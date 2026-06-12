package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Controller for editing the user's profile.
 * @author Adam Filinger
 * @version 1.0
 */
public class EditProfileController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(EditProfileController.class);

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private VBox businessFields;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField companyEmailField;
    @FXML
    private TextField phoneField;

    private Uzivatel user;
    private PravnickaOsoba business;
    private UserService userService = new UserService();

    @Override
    void onView(Uzivatel user) {
        this.user = user;
        this.business = userService.getBusinessForUser(user);
        populateFields();
        updateViewForUserType();
    }

    private void populateFields() {
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());

        if (business != null) {
            companyNameField.setText(business.getName());
            companyEmailField.setText(business.getEmail());
            phoneField.setText(business.getTel());
        }
    }

    private void updateViewForUserType() {
        businessFields.setVisible(business != null);
    }

    @FXML
    private void handleUpdateProfile() {
        try {
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());

            if (business != null) {
                business.setName(companyNameField.getText());
                business.setEmail(companyEmailField.getText());
                business.setTel(phoneField.getText());
            }

            userService.updateUser(user, business);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");
        } catch (Exception e) {
            logger.error("Failed to update profile for user: {}", user.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
        }
    }

    @FXML
    private void handleDeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.deleteUser(user);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account deleted successfully.");
                mainController.handleLogout();
            } catch (Exception e) {
                logger.error("Failed to delete account for user: {}", user.getId(), e);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete account.");
            }
        }
    }
}
