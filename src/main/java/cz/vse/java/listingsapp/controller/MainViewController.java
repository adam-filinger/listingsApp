package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Uzivatel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main view of the application after login.
 * @author Adam Filinger
 * @version 1.4
 */
public class MainViewController {

    @FXML
    private MenuItem registerBusinessMenuItem;
    @FXML
    private MenuItem addListingMenuItem;
    @FXML
    private MenuItem showMyListingsMenuItem;

    private Uzivatel user;
    private boolean isBusiness;

    /**
     * Sets the user and their business status.
     * @param user The user.
     * @param isBusiness Whether the user is a business.
     */
    public void setUser(Uzivatel user, boolean isBusiness) {
        this.user = user;
        this.isBusiness = isBusiness;
        updateUI();
    }

    /**
     * Updates the UI based on the user's business status.
     */
    private void updateUI() {
        if (isBusiness) {
            registerBusinessMenuItem.setVisible(false);
        } else {
            addListingMenuItem.setVisible(false);
            showMyListingsMenuItem.setVisible(false);
        }
    }

    /**
     * Shows a "not implemented" alert.
     */
    private void showNotImplementedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Not Implemented");
        alert.setHeaderText(null);
        alert.setContentText("This feature has not been implemented yet.");
        alert.showAndWait();
    }

    /**
     * Handles the "Show All Listings" button action.
     */
    @FXML
    private void handleShowAllListings() {
        showNotImplementedAlert();
    }

    /**
     * Handles the "Add Listing" button action.
     */
    @FXML
    private void handleAddListing() {
        showNotImplementedAlert();
    }

    /**
     * Handles the "Show My Listings" button action.
     */
    @FXML
    private void handleShowMyListings() {
        showNotImplementedAlert();
    }

    /**
     * Handles the "Show Offers" button action.
     */
    @FXML
    private void handleShowOffers() {
        showNotImplementedAlert();
    }

    /**
     * Handles the "Register Business" button action.
     */
    @FXML
    private void handleRegisterBusiness() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/bus_register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            BusRegisterController busRegisterController = fxmlLoader.getController();
            busRegisterController.setUser(user);
            Stage stage = (Stage) registerBusinessMenuItem.getParentPopup().getOwnerWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
