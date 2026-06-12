package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for displaying the user's listings.
 * @author Adam Filinger
 * @version 1.0
 */
public class MyListingsController extends Controller {

    @FXML
    private VBox listingsContainer;

    @FXML


    private Uzivatel user;
    private final ListingService listingService = new ListingService();

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
        CardCreator cc = new CardCreator();
        return cc.createListingCard(listing, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (mainController != null) {
                    mainController.showListingDetail(listing);
                }
            }
        });
    }
}
