package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Controller for displaying the user's offers.
 * @author Adam Filinger
 * @version 1.0
 */
public class MyOffersController extends Controller {

    @FXML
    private VBox offersContainer;

    private Uzivatel user;
    private ListingService listingService = new ListingService();

    @Override
    void onView(Poptavka listing, Uzivatel user) {
        this.user = user;
        loadOffers();
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        List<Nabidka> offers = listingService.getOffersByUser(user);

        if (offers.isEmpty()) {
            Label noOffersLabel = new Label("You haven't made any offers yet.");
            offersContainer.getChildren().add(noOffersLabel);
            return;
        }

        for (Nabidka offer : offers) {
            offersContainer.getChildren().add(createOfferCard(offer));
        }
    }

    private VBox createOfferCard(Nabidka offer) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #DDDDDD; -fx-border-radius: 3; -fx-padding: 10;");

        Label listingLabel = new Label("Offer for: " + offer.getPoptavka().getName());
        listingLabel.setFont(new Font("System Bold", 14));

        Label priceLabel = new Label(String.format("Proposed Price: $%.2f", offer.getProposedPrice()));
        Label messageLabel = new Label(offer.getText());
        messageLabel.setWrapText(true);

        card.getChildren().addAll(listingLabel, priceLabel, messageLabel);
        return card;
    }
}
