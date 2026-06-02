package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
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
 * @version 2.8
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

    private final Map<String, Pane> viewMap = new HashMap<>();
    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Map<String, String> viewPaths = new HashMap<>();
    private String currentView;

    @FXML
    private void initialize() {
        viewPaths.put("allListings", "/cz/vse/java/listingsapp/view/all-listings-view.fxml");
        viewPaths.put("addListing", "/cz/vse/java/listingsapp/view/add-listing-view.fxml");
        viewPaths.put("myListings", "/cz/vse/java/listingsapp/view/my-listings-view.fxml");
        viewPaths.put("myOffers", "/cz/vse/java/listingsapp/view/my-offers-view.fxml");
        viewPaths.put("registerBusiness", "/cz/vse/java/listingsapp/view/bus_register-view.fxml");
        viewPaths.put("listingDetail", "/cz/vse/java/listingsapp/view/listing-detail-view.fxml");
        viewPaths.put("editListing", "/cz/vse/java/listingsapp/view/edit-listing-view.fxml");
        viewPaths.put("createOffer", "/cz/vse/java/listingsapp/view/create-offer-view.fxml");
    }

    private void switchView(String viewName, Poptavka listing) {
        contentPane.getChildren().clear();

        if (viewMap.containsKey(viewName)) {
            Pane pane = viewMap.get(viewName);
            contentPane.getChildren().add(pane);
        } else {
            loadView(viewName);
        }
        try{
            controllerMap.get(viewName).onView(listing, user);
        } catch (Exception e) {
            logger.error("Error calling onView for {}", viewName, e);
        }
        currentView = viewName;
    }

    private void loadView(String viewName) {
        try {
            String viewPath = viewPaths.get(viewName);
            
            if (viewPath == null) {
                logger.error("No view path found for view: {}", viewName);
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            
            Controller controller = fxmlLoader.getController();
            if (controller != null) {
                try {
                    Method setUserMethod = controller.getClass().getMethod("setUser", Uzivatel.class);
                    setUserMethod.invoke(controller, user);
                } catch (NoSuchMethodException e) {
                    // It's okay if the controller doesn't have a setUser method
                } catch (Exception e) {
                    logger.error("Error setting user in controller for {}", viewPath, e);
                }
                controllerMap.put(viewName, controller);
                controllerMap.get(viewName).setMainController(this);
            }
            
            viewMap.put(viewName, pane);
            contentPane.getChildren().add(pane);
        } catch (IOException e) {
            logger.error("Failed to load view: {}", viewPaths.get(viewName), e);
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
        switchView("allListings", null);
    }

    /**
     * Handles the "Add Listing" button action.
     */
    @FXML
    private void handleAddListing() {
        switchView("addListing", null);
    }

    /**
     * Handles the "Show My Listings" button action.
     */
    @FXML
    private void handleShowMyListings() {
        switchView("myListings", null);
    }

    /**
     * Handles the "Show Offers" button action.
     */
    @FXML
    private void handleShowOffers() {
        switchView("myOffers", null);
    }

    /**
     * Handles the "Register Business" button action.
     */
    @FXML
    private void handleRegisterBusiness() {
        switchView("registerBusiness", null);
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

    /**
     * Shows the listing detail view.
     * @param listing The listing to display.
     */
    public void showListingDetail(Poptavka listing) {
        switchView("listingDetail", listing);
    }

    /**
     * Shows the edit listing view.
     * @param listing The listing to edit.
     */
    public void showEditListing(Poptavka listing) {
        switchView("editListing", listing);
    }

    /**
     * Shows the create offer view.
     * @param listing The listing to make an offer on.
     */
    public void showCreateOfferView(Poptavka listing) {
        switchView("createOffer", listing);
    }

    public void onView() {
        handleShowAllListings();
    }
}
