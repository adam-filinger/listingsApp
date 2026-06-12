package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Category;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.ListingService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for displaying all listings.
 * @author Adam Filinger
 * @version 1.7
 */
public class AllListingsController extends Controller implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(AllListingsController.class);

    @FXML
    private VBox listingsContainer;
    @FXML
    private ComboBox<Category> categoryFilter;
    @FXML
    private Label listingsTitle;

    private ListingService listingService;
    private Uzivatel user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listingService = new ListingService();
        categoryFilter.setItems(FXCollections.observableArrayList(Category.values()));
        categoryFilter.getItems().addFirst(null); // Add a "null" option to show all
        categoryFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadListings(newVal);
        });
    }
    
    @Override
    void onView(Uzivatel user) {
        this.user = user;
        if(mainController.getCurrentViewName().equals("myListings")){
            listingsTitle.setText("My Listings");
        }else{
            listingsTitle.setText("All Listings");
        }
        loadListings(null);
    }

    private void loadListings(Category category) {
        listingsContainer.getChildren().clear();
        List<Poptavka> listings = new ArrayList<>();
        try{
            if(mainController.getCurrentViewName().equals("myListings")) {
                listings = listingService.getListings(user);
            } else{
                listings = listingService.getListings();
            }
            if(category != null){
                listings = listings.stream()
                        .filter(listing -> listing.getCategory() == category)
                        .toList();

            }
        } catch (JDBCConnectionException e){
            showAlert(Alert.AlertType.ERROR, "DB connection error","Unable to connect to the database. Please try again later.");
            logger.error("Database connection error while loading listings", e);
        }


        if (listings.isEmpty()) {
            Label noListingsLabel = new Label("No listings available at the moment.");
            noListingsLabel.setFont(new Font(16));
            listingsContainer.getChildren().add(noListingsLabel);
            return;
        }

        for (Poptavka poptavka : listings) {
            listingsContainer.getChildren().add(createListingCard(poptavka));
        }
    }

    private VBox createListingCard(Poptavka listing) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 15;");

        card.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                if (mainController != null) {
                    mainController.showListingDetail(listing);
                }
            }
        });

        HBox titlePane = new HBox();
        Label titleLabel = new Label(listing.getName());
        titleLabel.setFont(new Font("System Bold", 18));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label priceLabel = new Label(String.format("$%.2f", listing.getPrice()));
        priceLabel.setFont(new Font("System Bold", 16));
        titlePane.getChildren().addAll(titleLabel, spacer, priceLabel);

        Label descriptionLabel = new Label(listing.getDescription());
        descriptionLabel.setWrapText(true);

        HBox footerPane = new HBox();
        Label companyLabel = new Label("by " + listing.getPravnickaOsoba().getName());
        companyLabel.setStyle("-fx-text-fill: #757575;");
        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Label dateLabel = new Label(dateFormat.format(listing.getCreatedDate()));
        dateLabel.setStyle("-fx-text-fill: #757575;");
        footerPane.getChildren().addAll(companyLabel, footerSpacer, dateLabel);

        card.getChildren().addAll(titlePane, descriptionLabel, footerPane);

        return card;
    }
}
