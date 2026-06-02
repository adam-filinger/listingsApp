package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.StatusNabidky;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
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
 * @version 1.0
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
    private ListingService listingService;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        listingService = new ListingService();
    }

    /**
     * Sets the listing and user for the offer.
     * @param listing The listing to make an offer on.
     * @param user The user making the offer.
     */
    @Override
    void onView(Poptavka listing, Uzivatel user) {
        this.listing = listing;
        this.user = user;
        listingTitleLabel.setText("For Listing: '" + listing.getName() + "'");
    }

    /**
     * Handles the "Submit Offer" button action.
     */
    @FXML
    private void handleSubmitOffer() {
        try {
            Nabidka offer = new Nabidka();
            offer.setPoptavka(listing);
            offer.setUzivatel(user);
            offer.setText(messageArea.getText());
            offer.setProposedPrice(Double.parseDouble(priceField.getText()));
            offer.setStatus(StatusNabidky.NOVA);

            if (listingService.saveNabidka(offer)) {
                showSuccessAlert();
                mainController.showListingDetail(listing);
            } else {
                showErrorAlert("Failed to save the offer.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid price format.");
        } catch (Exception e) {
            logger.error("Failed to submit offer for listing: {}", listing.getId(), e);
            showErrorAlert("An unexpected error occurred.");
        }
    }

    /**
     * Handles the "Cancel" button action.
     */
    @FXML
    private void handleCancel() {
        mainController.showListingDetail(listing);
    }

    /**
     * Shows an error alert.
     * @param message The error message.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a success alert.
     */
    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Your offer has been submitted successfully.");
        alert.showAndWait();
    }
}
