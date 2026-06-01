package cz.vse.java.listingsapp.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the form for creating a new listing or offer.
 * @author Adam Filinger
 * @version 1.0
 */
public class ListingForm {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final DoubleProperty price = new SimpleDoubleProperty(0.0);
    private final StringProperty text = new SimpleStringProperty("");
    private final DoubleProperty proposedPrice = new SimpleDoubleProperty(0.0);

    /**
     * @return the name property
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * @return the description property
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * @return the price property
     */
    public DoubleProperty priceProperty() {
        return price;
    }

    /**
     * @return the text property
     */
    public StringProperty textProperty() {
        return text;
    }

    /**
     * @return the proposed price property
     */
    public DoubleProperty proposedPriceProperty() {
        return proposedPrice;
    }
}
