package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import javafx.scene.control.Alert;

import java.util.ArrayList;

public abstract class Controller {
    MainViewController mainController;


    void onView(Uzivatel user) {

    }
    void onView(Poptavka listing, Uzivatel user) {

    }
    void onView(Nabidka offer, Uzivatel user) {

    }

    /**
     * Shows an alert.
     * @param message The error message to display.
     * @param alertType Type of alert to display.
     * @param title Title of the alert dialog.
     */
    void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    void setMainController(MainViewController controller){
        mainController = controller;
    };



}
