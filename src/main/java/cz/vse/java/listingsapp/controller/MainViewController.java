package cz.vse.java.listingsapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainViewController {

    private void showNotImplementedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Not Implemented");
        alert.setHeaderText(null);
        alert.setContentText("This feature has not been implemented yet.");
        alert.showAndWait();
    }

    @FXML
    private void handleShowAllListings() {
        showNotImplementedAlert();
    }

    @FXML
    private void handleAddListing() {
        showNotImplementedAlert();
    }

    @FXML
    private void handleShowMyListings() {
        showNotImplementedAlert();
    }

    @FXML
    private void handleShowOffers() {
        showNotImplementedAlert();
    }

    @FXML
    private void handleRegisterBusiness() {
        showNotImplementedAlert();
    }
}
