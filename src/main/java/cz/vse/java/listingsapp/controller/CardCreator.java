package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class CardCreator {

    public VBox createListingCard(Poptavka listing, EventHandler<MouseEvent> event) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 15;");

        card.setOnMouseClicked(event);

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
        DateTimeFormatter dateFormat = new DateTimeFormatterBuilder().appendPattern("MMM dd, yyyy").toFormatter();
        Label dateLabel = new Label(dateFormat.format(listing.getCreatedDate()));
        dateLabel.setStyle("-fx-text-fill: #757575;");
        footerPane.getChildren().addAll(companyLabel, footerSpacer, dateLabel);

        card.getChildren().addAll(titlePane, descriptionLabel, footerPane);

        return card;
    }

    public VBox createOfferCard(Nabidka offer, EventHandler<MouseEvent> event) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #DDDDDD; -fx-border-radius: 3; -fx-padding: 10;");

        card.setOnMouseClicked(event);

        Label userLabel = new Label("Offer from: " + offer.getUzivatel().getName());
        userLabel.setFont(new Font("System Bold", 14));

        Label priceLabel = new Label(String.format("Proposed Price: $%.2f", offer.getProposedPrice()));
        Label messageLabel = new Label(offer.getText());
        messageLabel.setWrapText(true);

        card.getChildren().addAll(userLabel, priceLabel, messageLabel);
        return card;
    }


}
