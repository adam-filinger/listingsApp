package cz.vse.java.listingsapp.controller.offerControllers;

import cz.vse.java.listingsapp.controller.Controller;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import jakarta.persistence.OptimisticLockException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML
    private Button deleteBussBtn;

    private Uzivatel user;
    private PravnickaOsoba business;
    private final UserService userService = new UserService();

    @Override
    protected void onView(Uzivatel user) {
        this.user = mainController.getUser().getKey();
        this.business = mainController.getUser().getValue();
        deleteBussBtn.setVisible(this.business != null);
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


            user = userService.updateUser(user, business);
            mainController.setUser(user);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully.");

        } catch (OptimisticLockException e){
            logger.warn("Optimistic lock failed for user: {}", user.getId(), e);
            showAlert(Alert.AlertType.WARNING, "Update conflict", "This user was modified by another user. Please refresh and try again.");
            user = userService.getUser(user);
            business = userService.getBusinessForUser(user);
            mainController.setUser(user);
        }
        catch (Exception e) {
            logger.error("Failed to update profile for user: {}", user.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile.");
        }
        mainController.handleShowAllListings();
    }


    private void handleDeleteAccount(String type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");



        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if ("user".equals(type)) {
                    userService.deleteUser(user);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Account deleted successfully.");
                    mainController.handleLogout();
                } else if ("business".equals(type)) {
                    userService.deleteBusiness(user);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Business deleted successfully.");
                    mainController.setUser(user);
                    mainController.onView();
                }

            } catch (OptimisticLockException e) {
                logger.warn("Optimistic lock failed for user: {}", user.getId(), e);
                showAlert(Alert.AlertType.WARNING, "Update conflict", "This user was modified by another user. Please refresh and try again.");
                user = userService.getUser(user);
                mainController.setUser(user);
            }
            catch (Exception e) {
                logger.error("Failed to delete account for user: {}", user.getId(), e);
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete account.");
            }
            mainController.handleShowAllListings();
        }
    }

    @FXML
    private void deleteAccount(){
        handleDeleteAccount("user");
    }
    @FXML
    private void deleteBusiness(){
        handleDeleteAccount("business");
    }



}
