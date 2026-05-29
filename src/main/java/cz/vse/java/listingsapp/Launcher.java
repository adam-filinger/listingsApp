package cz.vse.java.listingsapp;

import cz.vse.java.listingsapp.controller.ListingsApplication;
import cz.vse.java.listingsapp.service.DatabaseService;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        DatabaseService.getInstance().createTables();
        Application.launch(ListingsApplication.class, args);
    }
}
