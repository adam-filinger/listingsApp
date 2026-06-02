package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller for displaying the user's listings.
 * @author Adam Filinger
 * @version 1.0
 */
public class MyListingsController extends Controller {

    @FXML
    private VBox listingsContainer;

    private Uzivatel user;
    private ListingService listingService = new ListingService();

    @Override
    void onView(Poptavka listing, Uzivatel user) {
        this.user = user;
        loadListings();
    }

    private void loadListings() {
        listingsContainer.getChildren().clear();
        List<Poptavka> listings = listingService.getListings(user);

        if (listings.isEmpty()) {
            Label noListingsLabel = new Label("You haven't created any listings yet.");
            listingsContainer.getChildren().add(noListingsLabel);
            return;
        }

        for (Poptavka poptavka : listings) {
            listingsContainer.getChildren().add(createListingCard(poptavka));
        }
    }

    private VBox createListingCard(Poptavka listing) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 15;");

        card.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (mainController != null) {
                    mainController.showListingDetail(listing);
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

        card.getChildren().addAll(titlePane, descriptionLabel, footerPane);
        return card;
    }
}
