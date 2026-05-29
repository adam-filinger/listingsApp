package cz.vse.java.listingsapp.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ListingsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML from the correct resources path
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/vse/java/listingsapp/view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Listings App");
        stage.setScene(scene);
        stage.show();
    }
}
