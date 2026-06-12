package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.StatusNabidky;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for displaying the details of an offer.
 * @author Adam Filinger
 * @version 1.1
 */
public class OfferDetailController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(OfferDetailController.class);

    @FXML
    private Label listingTitleLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button modifyOfferButton;
    @FXML
    private Button acceptOfferButton;

    private Nabidka offer;
    private Uzivatel user;
    private OfferService offerService = new OfferService();

    @Override
    void onView(Nabidka offer, Uzivatel user) {
            this.offer = offer;
            this.user = user;
            displayOfferDetails();
            checkPermissions();
    }

    private void displayOfferDetails() {
        listingTitleLabel.setText("For Listing: '" + offer.getPoptavka().getName() + "'");
        userLabel.setText(offer.getUzivatel().getName());
        messageLabel.setText(offer.getText());
        priceLabel.setText(String.format("$%.2f", offer.getProposedPrice()));
        statusLabel.setText(offer.getStatus().toString());
    }

    private void checkPermissions() {
        boolean isOfferOwner = user.getId() == offer.getUzivatel().getId();
        boolean isListingOwner = user.getId() == offer.getPoptavka().getPravnickaOsoba().getUzivatel().getId();

        if (!isOfferOwner && !isListingOwner) {
            // User is not authorized to view this offer
            mainController.onView(); // Or some other appropriate view
            return;
        }

        modifyOfferButton.setVisible(isOfferOwner);
        acceptOfferButton.setVisible(isListingOwner);
    }

    @FXML
    private void handleModifyOffer() {
        if (mainController != null) {
            mainController.showEditOffer(offer);
        }
    }

    @FXML
    private void handleAcceptOffer() {
        try {
            offer.setStatus(StatusNabidky.PRIJATA);
            offerService.updateNabidka(offer);
            statusLabel.setText(offer.getStatus().toString());
            acceptOfferButton.setDisable(true);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Offer accepted successfully.");
        } catch (Exception e) {
            logger.error("Failed to accept offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to accept the offer.");
        }
    }

}
