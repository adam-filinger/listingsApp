package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.StatusNabidky;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for creating an offer on a listing.
 * @author Adam Filinger
 * @version 1.2
 */
public class CreateOfferController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(CreateOfferController.class);

    @FXML
    private Label listingTitleLabel;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField priceField;

    private Poptavka listing;
    private Uzivatel user;
    private OfferService offerService;

    public void initialize() {
        offerService = new OfferService();
    }

    @Override
    void onView(Poptavka listing, Uzivatel user) {
        this.listing = listing;
        this.user = user;
        listingTitleLabel.setText("For Listing: '" + listing.getName() + "'");
    }

    @FXML
    private void handleSubmitOffer() {
        try {
            Nabidka offer = new Nabidka();
            offer.setPoptavka(listing);
            offer.setUzivatel(user);
            offer.setText(messageArea.getText());
            offer.setProposedPrice(Double.parseDouble(priceField.getText()));
            offer.setStatus(StatusNabidky.NOVA);

            offerService.saveNabidka(offer);
            showAlert(Alert.AlertType.INFORMATION, "Offer Submitted", "Your offer has been submitted successfully.");
            mainController.showListingDetail(listing);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Price", "Please enter a valid number for the price.");
        } catch (Exception e) {
            logger.error("Failed to submit offer for listing: {}", listing.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting your offer. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        mainController.showListingDetail(listing);
    }

}
