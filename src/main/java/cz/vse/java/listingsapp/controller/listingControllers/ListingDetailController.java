package cz.vse.java.listingsapp.controller.listingControllers;

import cz.vse.java.listingsapp.controller.CardCreator;
import cz.vse.java.listingsapp.controller.Controller;
import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

/**
 * Controller for the listing detail view.
 * @author Adam Filinger
 * @version 1.6
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
    private final UserService userService = new UserService();
    private final OfferService offerService = new OfferService();

    @Override
    protected void onView(Poptavka listing, Uzivatel user) {
        this.listing = listing;
        this.user = mainController.getUser().getKey();
        displayListingDetails();
        updateButtonVisibility();
        loadOffers();
    }

    private void displayListingDetails() {
        titleLabel.setText(listing.getName());
        priceLabel.setText(String.format("$%.2f", listing.getPrice()));
        descriptionLabel.setText(listing.getDescription());
        companyLabel.setText(listing.getPravnickaOsoba().getName());
        DateTimeFormatter dateFormat = new DateTimeFormatterBuilder().appendPattern("MMM dd, yyyy").toFormatter();
        dateLabel.setText(dateFormat.format(listing.getCreatedDate()));
    }

    private void updateButtonVisibility() {
        if (user != null && (mainController.getUser().getValue() != null && mainController.getUser().getValue().getId() == listing.getPravnickaOsoba().getId())) {
            modifyButton.setVisible(true);
            makeOfferButton.setVisible(false);
        } else {
            modifyButton.setVisible(false);
            makeOfferButton.setVisible(true);
        }
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        List<Nabidka> offers;

        boolean isListingOwner = user.getId() == listing.getPravnickaOsoba().getUzivatel().getId();

        if (isListingOwner) {
            offers = offerService.getOffersForListing(listing);
        } else {
            offers = offerService.getOffersForListingByUser(listing, user);
        }

        if (offers.isEmpty()) {
            Label noOffersLabel = new Label("No offers to display.");
            offersContainer.getChildren().add(noOffersLabel);
            return;
        }

        for (Nabidka offer : offers) {
            offersContainer.getChildren().add(createOfferCard(offer));
        }
    }

    private VBox createOfferCard(Nabidka offer) {
       CardCreator cc = new CardCreator();
       return cc.createOfferCard(offer, event -> {
           if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
               if (mainController != null) {
                   mainController.showOfferDetail(offer);
               }
           }
       });
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
