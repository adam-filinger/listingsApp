package cz.vse.java.listingsapp.controller;

import cz.vse.java.listingsapp.model.Nabidka;
import cz.vse.java.listingsapp.model.Poptavka;
import cz.vse.java.listingsapp.model.Uzivatel;

import java.util.ArrayList;

public abstract class Controller {
    MainViewController mainController;


    void onView(Uzivatel user) {

    }
    void onView(Poptavka listing, Uzivatel user) {

    }
    void onView(Nabidka offer, Uzivatel user) {

    }



    void setMainController(MainViewController controller){
        mainController = controller;
    };



}
