package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller for the listing detail view.
 * @author Adam Filinger
 * @version 1.3
 */
public class ListingDetailController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(ListingDetailController.class);

    @FXML
    private Label titleLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label companyLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button modifyButton;
    @FXML
    private Button makeOfferButton;
    @FXML
    private VBox offersContainer;

    private Poptavka listing;
    private Uzivatel user;
    private UserService userService = new UserService();
    private ListingService listingService = new ListingService();

    @Override
    public void onView(Poptavka listing, Uzivatel user) {
        this.listing = listing;
        this.user = user;
        displayListingDetails();
        updateButtonVisibility();
        loadOffers();
    }

    private void displayListingDetails() {
        titleLabel.setText(listing.getName());
        priceLabel.setText(String.format("$%.2f", listing.getPrice()));
        descriptionLabel.setText(listing.getDescription());
        companyLabel.setText(listing.getPravnickaOsoba().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        dateLabel.setText(dateFormat.format(listing.getCreatedDate()));
    }

    private void updateButtonVisibility() {
        if (user != null && (userService.isBusiness(user) && user.getId() == listing.getPravnickaOsoba().getId())) {
            modifyButton.setVisible(true);
            makeOfferButton.setVisible(false);
        } else {
            modifyButton.setVisible(false);
            makeOfferButton.setVisible(true);
        }
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        List<Nabidka> offers = listingService.getOffersForListing(listing);

        if (offers.isEmpty()) {
            Label noOffersLabel = new Label("No offers have been made for this listing yet.");
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

        Label userLabel = new Label("Offer from: " + offer.getUzivatel().getName());
        userLabel.setFont(new Font("System Bold", 14));

        Label priceLabel = new Label(String.format("Proposed Price: $%.2f", offer.getProposedPrice()));
        Label messageLabel = new Label(offer.getText());
        messageLabel.setWrapText(true);

        card.getChildren().addAll(userLabel, priceLabel, messageLabel);
        return card;
    }

    @FXML
    private void handleModify() {
        if (mainController != null) {
            mainController.showEditListing(listing);
        }
    }

    @FXML
    private void handleMakeOffer() {
        if (mainController != null) {
            mainController.showCreateOfferView(listing);
        }
    }
}
