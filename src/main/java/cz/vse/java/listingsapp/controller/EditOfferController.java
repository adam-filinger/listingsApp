package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for editing an offer.
 * @author Adam Filinger
 * @version 1.0
 */
public class EditOfferController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(EditOfferController.class);

    @FXML
    private TextArea messageArea;
    @FXML
    private TextField priceField;

    private Nabidka offer;
    private OfferService offerService = new OfferService();

    @Override
    void onView(Nabidka offer, Uzivatel user) {
        this.offer = offer;
        populateFields();
    }

    private void populateFields() {
        messageArea.setText(offer.getText());
        priceField.setText(String.valueOf(offer.getProposedPrice()));
    }

    @FXML
    private void handleUpdateOffer() {
        try {
            offer.setText(messageArea.getText());
            offer.setProposedPrice(Double.parseDouble(priceField.getText()));

            offerService.updateNabidka(offer);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Offer updated successfully.");
            mainController.showOfferDetail(offer);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format.");
        } catch (Exception e) {
            logger.error("Failed to update offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the offer.");
        }
    }

    @FXML
    private void handleCancel() {
        mainController.showOfferDetail(offer);
    }

}
