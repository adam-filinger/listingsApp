package cz.vse.java.listingsapp.controller.offerControllers;

import cz.vse.java.listingsapp.controller.Controller;
import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import jakarta.persistence.OptimisticLockException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.hibernate.exception.JDBCConnectionException;
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
    private final OfferService offerService = new OfferService();

    @Override
    protected void onView(Nabidka offer, Uzivatel user) {
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

            offer = offerService.updateNabidka(offer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Offer updated successfully.");


        }
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid price.");
        } catch (OptimisticLockException e) {
            logger.warn("Optimistic lock failed for offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.WARNING, "Update conflict", "This offer was modified by another user. Please refresh and try again.");
            offer = offerService.getOfferById(offer);
            mainController.showOfferDetail(offer);
        }
        catch(JDBCConnectionException e){
            logger.error("JDBC connection failed while trying to update offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR, "JDBC Connection Error", "JDBC connection failed, please try again.");
        }
        catch (Exception e) {
            logger.error("Failed to update offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR,"Update error.", "Failed to update offer");
        }
        mainController.showOfferDetail(offer);
    }

    @FXML
    private void deleteOffer(){
        try {
            offerService.deleteOffer(offer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Offer deleted successfully.");
        } catch (OptimisticLockException e) {
            logger.warn("Optimistic lock failed for offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.WARNING, "Update conflict", "This offer was modified by another user. Please refresh and try again.");
            offer = offerService.getOfferById(offer);
            mainController.showOfferDetail(offer);
        } catch (JDBCConnectionException e){
            logger.error("JDBC connection failed while trying to delete offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR, "JDBC Connection Error", "JDBC connection failed, please try again.");
        }
        catch (Exception e) {
            logger.error("Failed to delete offer: {}", offer.getId(), e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete offer.");
        }
         mainController.showListingDetail(offer.getPoptavka());
    }


    @FXML
    private void handleCancel() {
        mainController.showOfferDetail(offer);
    }

}
