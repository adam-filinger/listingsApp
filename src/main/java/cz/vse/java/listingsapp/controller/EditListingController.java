package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import jakarta.persistence.OptimisticLockException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for editing a listing.
 * @author Adam Filinger
 * @version 1.0
 */
public class EditListingController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(EditListingController.class);

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField priceField;

    private Poptavka listing;
    private ListingService listingService;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        listingService = new ListingService();
    }

    /**
     * Sets the listing to be edited.
     * @param listing The listing.
     */
    public void setListing(Poptavka listing) {
        this.listing = listing;
        populateFields();
    }


    /**
     * Populates the input fields with the listing's data.
     */
    private void populateFields() {
        titleField.setText(listing.getName());
        descriptionArea.setText(listing.getDescription());
        priceField.setText(String.valueOf(listing.getPrice()));
    }

    /**
     * Handles the "Update Listing" button action.
     */
    @FXML
    private void handleUpdate() {
        try {
            listing.setName(titleField.getText());
            listing.setDescription(descriptionArea.getText());
            listing.setPrice(Double.parseDouble(priceField.getText()));

            listingService.updatePoptavka(listing);

            // Navigate back to the detail view
            mainController.showListingDetail(listing);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid price.");
        } catch (OptimisticLockException e) {
            logger.warn("Optimistic lock failed for listing: {}", listing.getId(), e);
            showAlert(Alert.AlertType.WARNING, "Update conflict", "This listing was modified by another user. Please refresh and try again.");
            // Optionally, refresh the listing data
            mainController.showListingDetail(listingService.findPoptavkaById(listing.getId()));
        }
        catch(JDBCConnectionException e){
            logger.error("JDBC connection failed while trying to update listing: {}", listing.getId(), e);
            showAlert(Alert.AlertType.ERROR, "JDBC Connection Error", "JDBC connection failed, please try again.");
        }
        catch (Exception e) {
            logger.error("Failed to update listing: {}", listing.getId(), e);
            showAlert(Alert.AlertType.ERROR,"Update error.", "Failed to update listing");
        }
    }

    /**
     * Handles the "Cancel" button action.
     */
    @FXML
    private void handleCancel() {
        mainController.showListingDetail(listing);
    }



    @Override
    public void onView(Poptavka listing, Uzivatel user) {
        setListing(listing);
    }
}
