package cz.vse.java.listingsapp.controller.offerControllers;

import cz.vse.java.listingsapp.controller.CardCreator;
import cz.vse.java.listingsapp.controller.Controller;
import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;
import cz.vse.java.listingsapp.service.OfferService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controller for displaying the user's offers.
 * @author Adam Filinger
 * @version 1.2
 */
public class MyOffersController extends Controller {

    @FXML
    private VBox offersContainer;

    private Uzivatel user;
    private final OfferService offerService = new OfferService();

    @Override
    protected void onView(Uzivatel user) {
        this.user = user;
        loadOffers();
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        List<Nabidka> offers = offerService.getOffersByUser(user);

        if (offers.isEmpty()) {
            Label noOffersLabel = new Label("You haven't made any offers yet.");
            offersContainer.getChildren().add(noOffersLabel);
            return;
        }

        for (Nabidka offer : offers) {
            offersContainer.getChildren().add(createOfferCard(offer));
        }
    }

    private VBox createOfferCard(Nabidka offer) {
        CardCreator cc = new CardCreator();
        return cc.createOfferCard(offer, event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                mainController.showOfferDetail(offer);
            }
        });
    }
}
