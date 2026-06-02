package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;

import java.util.ArrayList;

public abstract class Controller {
    MainViewController mainController;

    void onView(Poptavka listing, Uzivatel user) {

    }

    void setMainController(MainViewController controller){
        mainController = controller;
    };


}
