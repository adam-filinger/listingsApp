package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Uzivatel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the main view of the application after login.
 * @author Adam Filinger
 * @version 2.2
 */
public class MainViewController {
    
    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    private MenuItem registerBusinessMenuItem;
    @FXML
    private MenuItem addListingMenuItem;
    @FXML
    private MenuItem myListingsMenuItem;
    @FXML
    private MenuItem allListingsMenuItem;
    @FXML
    private MenuItem offersMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private StackPane contentPane;

    private Uzivatel user;
    private boolean isBusiness;

    private final Map<MenuItem, Pane> viewMap = new HashMap<>();
    private final Map<MenuItem, String> viewPaths = new HashMap<>();
    private MenuItem currentMenuItem;

    @FXML
    private void initialize() {
        viewPaths.put(allListingsMenuItem, "/cz/vse/java/listingsapp/view/all-listings-view.fxml");
        viewPaths.put(addListingMenuItem, "/cz/vse/java/listingsapp/view/add-listing-view.fxml");
        viewPaths.put(myListingsMenuItem, "/cz/vse/java/listingsapp/view/my-listings-view.fxml");
        viewPaths.put(offersMenuItem, "/cz/vse/java/listingsapp/view/offers-view.fxml");
        viewPaths.put(registerBusinessMenuItem, "/cz/vse/java/listingsapp/view/bus_register-view.fxml");

        handleShowAllListings();
    }

    private void switchView(MenuItem menuItem) {
        if (currentMenuItem != null) {
            currentMenuItem.setDisable(false);
        }

        contentPane.getChildren().clear();

        if (viewMap.containsKey(menuItem)) {
            Pane pane = viewMap.get(menuItem);
            contentPane.getChildren().add(pane);
        } else {
            loadView(menuItem);
        }

        currentMenuItem = menuItem;
        currentMenuItem.setDisable(true);
    }

    private void loadView(MenuItem menuItem) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPaths.get(menuItem)));
            Pane pane = fxmlLoader.load();
            
            Object controller = fxmlLoader.getController();
            if (controller != null) {
                try {
                    Method setUserMethod = controller.getClass().getMethod("setUser", Uzivatel.class);
                    setUserMethod.invoke(controller, user);
                } catch (NoSuchMethodException e) {
                    // It's okay if the controller doesn't have a setUser method
                } catch (Exception e) {
                    logger.error("Error setting user in controller for {}", viewPaths.get(menuItem), e);
                }
            }
            
            viewMap.put(menuItem, pane);
            contentPane.getChildren().add(pane);
        } catch (IOException e) {
            logger.error("Failed to load view: {}", viewPaths.get(menuItem), e);
            showErrorAlert();
        }
    }

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
            myListingsMenuItem.setVisible(false);
        }
    }
    
    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to load view.");
        alert.showAndWait();
    }

    /**
     * Handles the "Show All Listings" button action.
     */
    @FXML
    private void handleShowAllListings() {
        switchView(allListingsMenuItem);
    }

    /**
     * Handles the "Add Listing" button action.
     */
    @FXML
    private void handleAddListing() {
        switchView(addListingMenuItem);
    }

    /**
     * Handles the "Show My Listings" button action.
     */
    @FXML
    private void handleShowMyListings() {
        switchView(myListingsMenuItem);
    }

    /**
     * Handles the "Show Offers" button action.
     */
    @FXML
    private void handleShowOffers() {
        switchView(offersMenuItem);
    }

    /**
     * Handles the "Register Business" button action.
     */
    @FXML
    private void handleRegisterBusiness() {
        switchView(registerBusinessMenuItem);
    }

    /**
     * Handles the "Logout" button action.
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            logger.error("Failed to load login view.", e);
            showErrorAlert();
        }
    }
}