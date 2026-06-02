package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import jakarta.persistence.OptimisticLockException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private MainViewController mainViewController;

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
     * Sets the main view controller.
     * @param mainViewController The main view controller.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
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
            mainViewController.showListingDetail(listing);
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid price format.");
        } catch (OptimisticLockException e) {
            logger.warn("Optimistic lock failed for listing: {}", listing.getId(), e);
            showErrorAlert("This listing has been modified by another user. Please reload and try again.");
            // Optionally, refresh the listing data
            mainViewController.showListingDetail(listingService.findPoptavkaById(listing.getId()));
        } catch (Exception e) {
            logger.error("Failed to update listing: {}", listing.getId(), e);
            showErrorAlert("Failed to update the listing.");
        }
    }

    /**
     * Handles the "Cancel" button action.
     */
    @FXML
    private void handleCancel() {
        mainViewController.showListingDetail(listing);
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

    @Override
    public void onView(Poptavka listing, Uzivatel user) {
        setListing(listing);
    }
}
