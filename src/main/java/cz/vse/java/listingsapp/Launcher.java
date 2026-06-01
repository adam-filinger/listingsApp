package cz.vse.java.listingsapp;

import cz.vse.java.listingsapp.controller.ListingsApplication;
import javafx.application.Application;

/**
 * The main class of the application.
 * @author Adam Filinger
 * @version 1.0
 */
public class Launcher {
    /**
     * The main method of the application.
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(ListingsApplication.class, args);
    }
}
