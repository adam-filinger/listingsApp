package cz.vse.java.listingsapp.controller.listingControllers;

import cz.vse.java.listingsapp.model.Category;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the form for creating a new listing or offer.
 * @author Adam Filinger
 * @version 1.1
 */
public class ListingForm {
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final DoubleProperty price = new SimpleDoubleProperty(0.0);
    private final StringProperty text = new SimpleStringProperty("");
    private final DoubleProperty proposedPrice = new SimpleDoubleProperty(0.0);
    private final ObjectProperty<Category> category = new SimpleObjectProperty<>();
    private final StringProperty tags = new SimpleStringProperty("");

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty textProperty() {
        return text;
    }

    public DoubleProperty proposedPriceProperty() {
        return proposedPrice;
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }


    public void resetForm(){
        nameProperty().set("");
        descriptionProperty().set("");
        priceProperty().set(0.0);
        textProperty().set("");
        proposedPriceProperty().set(0.0);
        categoryProperty().set(null);
    }

}
