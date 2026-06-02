package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Controller for the listing detail view.
 * @author Adam Filinger
 * @version 1.1
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
    private Button answerButton;

    private Poptavka listing;
    private Uzivatel user;
    private MainViewController mainViewController;
    private UserService us = new UserService();

    /**
     * Initializes the controller with the listing data.
     * @param listing The listing to display.
     * @param user The current user.
     */
    public void setListing(Poptavka listing, Uzivatel user) {
        this.listing = listing;
        this.user = user;
        displayListingDetails();
        updateButtonVisibility();
    }

    /**
     * Sets the main view controller.
     * @param mainViewController The main view controller.
     */
    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    /**
     * Populates the view with the listing's details.
     */
    private void displayListingDetails() {
        titleLabel.setText(listing.getName());
        priceLabel.setText(String.format("$%.2f", listing.getPrice()));
        descriptionLabel.setText(listing.getDescription());
        companyLabel.setText(listing.getPravnickaOsoba().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        dateLabel.setText(dateFormat.format(listing.getCreatedDate()));
    }

    /**
     * Shows or hides buttons based on user ownership of the listing.
     */
    private void updateButtonVisibility() {
        if (user != null && (us.isBusiness(user) && user.getId() == listing.getPravnickaOsoba().getId())) {
            modifyButton.setVisible(true);
            answerButton.setVisible(false);
        } else {
            modifyButton.setVisible(false);
            answerButton.setVisible(true);
        }
    }

    /**
     * Handles the "Modify Listing" button action.
     */
    @FXML
    private void handleModify() {
        if (mainViewController != null) {
            mainViewController.showEditListing(listing);
        }
    }

    /**
     * Handles the "Answer Listing" button action.
     */
    @FXML
    private void handleAnswer() {
        // Placeholder for answering logic
        logger.info("Answer button clicked for listing: " + listing.getName());
    }

    @Override
    public void onView(Poptavka listing, Uzivatel user) {
        setListing(listing, user);
    }
}
