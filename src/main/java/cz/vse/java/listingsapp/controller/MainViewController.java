package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the main view of the application after login.
 * @author Adam Filinger
 * @version 3.1
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
    private MenuItem editProfileMenuItem;
    @FXML
    private StackPane contentPane;

    private Pair<Uzivatel, PravnickaOsoba> user;

    private final Map<String, Pane> viewMap = new HashMap<>();
    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Map<String, String> viewPaths = new HashMap<>();
    private String currentView = "allListings";

    @FXML
    private void initialize() {
        viewPaths.put("allListings", "/cz/vse/java/listingsapp/view/all-listings-view.fxml");
        viewPaths.put("addListing", "/cz/vse/java/listingsapp/view/add-listing-view.fxml");
        viewPaths.put("myListings", "/cz/vse/java/listingsapp/view/all-listings-view.fxml");
        viewPaths.put("myOffers", "/cz/vse/java/listingsapp/view/my-offers-view.fxml");
        viewPaths.put("registerBusiness", "/cz/vse/java/listingsapp/view/bus_register-view.fxml");
        viewPaths.put("listingDetail", "/cz/vse/java/listingsapp/view/listing-detail-view.fxml");
        viewPaths.put("editListing", "/cz/vse/java/listingsapp/view/edit-listing-view.fxml");
        viewPaths.put("createOffer", "/cz/vse/java/listingsapp/view/create-offer-view.fxml");
        viewPaths.put("offerDetail", "/cz/vse/java/listingsapp/view/offer-detail-view.fxml");
        viewPaths.put("editOffer", "/cz/vse/java/listingsapp/view/edit-offer-view.fxml");
        viewPaths.put("editProfile", "/cz/vse/java/listingsapp/view/edit-profile-view.fxml");
    }

    private void switchView(String viewName, Object data) {
        contentPane.getChildren().clear();

        if (viewMap.containsKey(viewName)) {
            Pane pane = viewMap.get(viewName);
            contentPane.getChildren().add(pane);
        } else {
            loadView(viewName);
        }
        currentView = viewName;
        try{
            switch (data) {
                case Poptavka poptavka -> controllerMap.get(viewName).onView(poptavka, user.getKey());
                case Nabidka nabidka -> controllerMap.get(viewName).onView(nabidka, user.getKey());
                case null -> controllerMap.get(viewName).onView(user.getKey());
                default -> throw new IllegalStateException("Unexpected value: " + data);
            }
        } catch (Exception e) {
            logger.error("Error calling onView for {}", viewName, e);
        }
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
                controllerMap.put(viewName, controller);
                controller.setMainController(this);
            }
            
            viewMap.put(viewName, pane);
            contentPane.getChildren().add(pane);
        } catch (IOException e) {
            logger.error("Failed to load view: {}", viewPaths.get(viewName), e);
            showErrorAlert();
        }
    }

    public void setUser(Uzivatel user) {
        UserService userService = new UserService();
        if(userService.isBusiness(user)) {
            PravnickaOsoba po = userService.getBusinessForUser(user);
            this.user = new Pair<>(user, po);
        }else{
            this.user = new Pair<>(user, null);
        }
        updateUI();
    }

    private void updateUI() {
        if (user.getValue() != null) {
            registerBusinessMenuItem.setVisible(false);
        } else {
            registerBusinessMenuItem.setVisible(true);
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

    @FXML
    public void handleShowAllListings() {
        switchView("allListings", null);
    }

    @FXML
    private void handleAddListing() {
        switchView("addListing", null);
    }

    @FXML
    private void handleShowMyListings() {
        switchView("myListings", null);
    }

    @FXML
    private void handleShowOffers() {
        switchView("myOffers", null);
    }

    @FXML
    private void handleRegisterBusiness() {
        switchView("registerBusiness", null);
    }
    
    @FXML
    private void handleEditProfile() {
        switchView("editProfile", null);
    }

    @FXML
    public void handleLogout() {
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

    public void showListingDetail(Poptavka listing) {
        switchView("listingDetail", listing);
    }

    public void showEditListing(Poptavka listing) {
        switchView("editListing", listing);
    }

    public void showCreateOfferView(Poptavka listing) {
        switchView("createOffer", listing);
    }

    public void showOfferDetail(Nabidka offer) {
        switchView("offerDetail", offer);
    }

    public void showEditOffer(Nabidka offer) {
        switchView("editOffer", offer);
    }

    public void onView() {
        handleShowAllListings();
    }

    public String getCurrentViewName(){
        return currentView;
    }

    public Pair<Uzivatel, PravnickaOsoba> getUser() {
        return user;
    }
}
