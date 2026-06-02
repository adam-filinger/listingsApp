package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.structure.DoubleField;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.StatusNabidky;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import cz.vse.java.listingsapp.service.JPAProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller for adding a new listing or offer.
 * @author Adam Filinger
 * @version 1.1
 */
public class AddListingController extends Controller implements Initializable  {

    private static final Logger logger = LoggerFactory.getLogger(AddListingController.class);

    @FXML
    public BorderPane rootPane;

    private final ListingForm listingForm = new ListingForm();
    private Form form;
    private ListingService listingService;
    private Uzivatel user;
    private boolean isListing = true; // Default to listing
    private Poptavka targetListing; // Used if this is an offer for a listing

    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listingService = new ListingService();
        // The form will be built later when isListing is known, or we can build a default one.
        // We will build the default form here.
        buildForm();
    }

    /**
     * Sets the user for the controller.
     * @param user The user.
     */
    public void setUser(Uzivatel user) {
        this.user = user;
    }

    /**
     * Sets whether this form is for a listing or an offer.
     * @param isListing True for listing, false for offer.
     * @param targetListing The target listing if it's an offer (can be null for listing).
     */
    public void setIsListing(boolean isListing, Poptavka targetListing) {
        this.isListing = isListing;
        this.targetListing = targetListing;
        buildForm(); // Rebuild form based on the flag
    }

    /**
     * Builds the form based on the isListing flag.
     */
    private void buildForm() {
        if (rootPane.getCenter() != null) {
            rootPane.setCenter(null);
        }

        Group formGroup;

        if (isListing) {
            StringField nameField = Field.ofStringType(listingForm.nameProperty())
                    .label("Listing Name")
                    .required("This field is required");

            StringField descriptionField = Field.ofStringType(listingForm.descriptionProperty())
                    .label("Description")
                    .multiline(true)
                    .required("This field is required");

            DoubleField priceField = Field.ofDoubleType(listingForm.priceProperty())
                    .label("Price")
                    .required("This field is required");

            formGroup = Group.of(nameField, descriptionField, priceField);
            form = Form.of(formGroup).title("Add Listing");
        } else {
            StringField textField = Field.ofStringType(listingForm.textProperty())
                    .label("Offer Text")
                    .multiline(true)
                    .required("This field is required");

            DoubleField proposedPriceField = Field.ofDoubleType(listingForm.proposedPriceProperty())
                    .label("Proposed Price")
                    .required("This field is required");

            formGroup = Group.of(textField, proposedPriceField);
            form = Form.of(formGroup).title("Add Offer");
        }

        rootPane.setCenter(new FormRenderer(form));
    }

    /**
     * Handles the add button action.
     */
    @FXML
    private void addListing() {
        form.persist();

        if (!form.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        if (isListing) {
            saveListing();
        } else {
            saveOffer();
        }
    }

    private void saveListing() {
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "User not logged in.");
            return;
        }

        PravnickaOsoba pravnickaOsoba = getBusinessForUser(user);
        if (pravnickaOsoba == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Only business users can add listings.");
            return;
        }

        Poptavka poptavka = new Poptavka();
        poptavka.setName(listingForm.nameProperty().get());
        poptavka.setDescription(listingForm.descriptionProperty().get());
        poptavka.setPrice(listingForm.priceProperty().get());
        poptavka.setCreatedDate(new Date());
        poptavka.setPravnickaOsoba(pravnickaOsoba);

        if (listingService.savePoptavka(poptavka)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Listing added successfully.");
            // Optional: reset form or navigate away
            form.reset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add listing.");
        }
    }

    private void saveOffer() {
        if (user == null || targetListing == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing user or target listing.");
            return;
        }

        Nabidka nabidka = new Nabidka();
        nabidka.setText(listingForm.textProperty().get());
        nabidka.setProposedPrice(listingForm.proposedPriceProperty().get());
        nabidka.setStatus(StatusNabidky.NOVA); 
        nabidka.setUzivatel(user);
        nabidka.setPoptavka(targetListing);

        if (listingService.saveNabidka(nabidka)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Offer added successfully.");
            form.reset();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add offer.");
        }
    }

    /**
     * Gets the PravnickaOsoba associated with the given user.
     * @param user The user.
     * @return The PravnickaOsoba, or null if not found.
     */
    private PravnickaOsoba getBusinessForUser(Uzivatel user) {
        EntityManager em = JPAProvider.getInstance().getEntityManager();
        try {
            TypedQuery<PravnickaOsoba> query = em.createQuery(
                    "SELECT p FROM PravnickaOsoba p WHERE p.uzivatel = :user", PravnickaOsoba.class);
            query.setParameter("user", user);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * Shows an alert dialog.
     * @param alertType The type of the alert.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}