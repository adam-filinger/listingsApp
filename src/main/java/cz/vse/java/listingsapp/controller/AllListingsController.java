package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for displaying all listings.
 * @author Adam Filinger
 * @version 1.4
 */
public class AllListingsController implements Initializable, Controller {

    private static final Logger logger = LoggerFactory.getLogger(AllListingsController.class);

    @FXML
    private VBox listingsContainer;

    private ListingService listingService;
    private Uzivatel user;
    private MainViewController mainViewController;

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listingService = new ListingService();
        
        listingsContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                loadListings();
            }
        });

        loadListings();
    }

    /**
     * Sets the user for the controller.
     * @param user The user.
     */
    public void setUser(Uzivatel user) {
        this.user = user;
    }

    /**
     * Sets the main view controller.
     * @param mainViewController The main view controller.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * Loads all listings from the service and displays them.
     */
    private void loadListings() {
        listingsContainer.getChildren().clear();
        List<Poptavka> listings = listingService.getAllPoptavky();

        if (listings.isEmpty()) {
            Label noListingsLabel = new Label("No listings available at the moment.");
            noListingsLabel.setFont(new Font(16));
            listingsContainer.getChildren().add(noListingsLabel);
            return;
        }

        for (Poptavka listing : listings) {
            listingsContainer.getChildren().add(createListingCard(listing));
        }
    }

    /**
     * Creates a card view for a single listing.
     * @param listing The listing to display.
     * @return A VBox representing the listing card.
     */
    private VBox createListingCard(Poptavka listing) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 15;");

        card.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (mainViewController != null) {
                    mainViewController.showListingDetail(listing);
                }
            }
        });

        HBox titlePane = new HBox();
        Label titleLabel = new Label(listing.getName());
        titleLabel.setFont(new Font("System Bold", 18));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label priceLabel = new Label(String.format("$%.2f", listing.getPrice()));
        priceLabel.setFont(new Font("System Bold", 16));
        titlePane.getChildren().addAll(titleLabel, spacer, priceLabel);

        Label descriptionLabel = new Label(listing.getDescription());
        descriptionLabel.setWrapText(true);

        HBox footerPane = new HBox();
        Label companyLabel = new Label("by " + listing.getPravnickaOsoba().getName());
        companyLabel.setStyle("-fx-text-fill: #757575;");
        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Label dateLabel = new Label(dateFormat.format(listing.getCreatedDate()));
        dateLabel.setStyle("-fx-text-fill: #757575;");
        footerPane.getChildren().addAll(companyLabel, footerSpacer, dateLabel);

        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        Button makeOfferButton = new Button("Make Offer");
        makeOfferButton.setOnAction(event -> handleMakeOffer(listing));
        buttonPane.getChildren().add(makeOfferButton);

        card.getChildren().addAll(titlePane, descriptionLabel, footerPane, buttonPane);
        return card;
    }

    /**
     * Handles the action of making an offer on a listing.
     * @param listing The listing to make an offer on.
     */
    private void handleMakeOffer(Poptavka listing) {
        logger.info("User wants to make an offer on listing: " + listing.getName());
    }

    @Override
    public void onView() {

    }
}
