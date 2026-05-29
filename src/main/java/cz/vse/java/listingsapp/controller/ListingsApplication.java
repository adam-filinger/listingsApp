package cz.vse.java.listingsapp.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ListingsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Use getClass().getResource() for a more robust path to the FXML file
        URL fxmlLocation = getClass().getResource("/cz/vse/java/listingsapp/view/login-view.fxml");
        if (fxmlLocation == null) {
            throw new IOException("Cannot find FXML file. Please check the path.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Listings App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
