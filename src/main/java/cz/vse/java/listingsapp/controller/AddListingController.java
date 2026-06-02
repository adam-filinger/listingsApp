package cz.vse.java.listingsapp.controller;

import com.dlsc.formsfx.model.event.FormEvent;
import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import cz.vse.java.listingsapp.model.Category;
import cz.vse.java.listingsapp.model.ListingTag;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.PravnickaOsoba;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import cz.vse.java.listingsapp.service.JPAProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for adding a new listing.
 * @author Adam Filinger
 * @version 1.4
 */
public class AddListingController extends Controller implements Initializable  {

    private static final Logger logger = LoggerFactory.getLogger(AddListingController.class);

    @FXML
    public BorderPane rootPane;

    private final ListingForm listingForm = new ListingForm();
    private Form form;
    private ListingService listingService;
    private Uzivatel user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listingService = new ListingService();
        buildForm();
    }

    @Override
    void onView(Uzivatel user) {
        this.user = user;
    }

    private void buildForm() {
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


        SingleSelectionField<Category> categoryField = Field.ofSingleSelectionType(Arrays.asList(Category.values()), 0)
                .label("Category")
                .required("This field is required");

        StringField tagsField = Field.ofStringType(listingForm.tagsProperty())
                .label("Tags (comma-separated)");

        Group formGroup = Group.of(nameField, descriptionField, priceField, categoryField, tagsField);
        form = Form.of(formGroup).title("Add Listing");
        form.addEventHandler(FormEvent.EVENT_FORM_PERSISTED,e -> listingForm.categoryProperty().setValue(categoryField.getSelection()));

        rootPane.setCenter(new FormRenderer(form));
    }

    @FXML
    private void addListing() {
        form.persist();

        if (!form.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please correct the errors in the form.");
            return;
        }

        saveListing();
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
        poptavka.setCategory(listingForm.categoryProperty().get());
        poptavka.setCreatedDate(new Date());
        poptavka.setPravnickaOsoba(pravnickaOsoba);

        Set<ListingTag> tags = Arrays.stream(listingForm.tagsProperty().get().split(","))
                .map(String::trim)
                .filter(tagName -> !tagName.isEmpty())
                .map(tagName -> {
                    ListingTag tag = new ListingTag();
                    tag.setName(tagName);
                    return tag;
                })
                .collect(Collectors.toSet());
        poptavka.setTags(tags);

        try {
            listingService.savePoptavka(poptavka);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Listing added successfully.");
            form.reset();
        } catch (Exception e) {
            logger.error("Failed to add listing", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add listing.");
        }
    }

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
