module cz.vse.java.listingsapp.listingsapp {
    requires javafx.controls;
    requires javafx.fxml;


    requires com.dlsc.formsfx;
    requires java.sql;
    requires jbcrypt;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.slf4j;
    requires com.h2database;

    opens cz.vse.java.listingsapp.model to org.hibernate.orm.core;
    opens cz.vse.java.listingsapp.controller to javafx.fxml;

    exports cz.vse.java.listingsapp.controller;
    exports cz.vse.java.listingsapp.model;
    exports cz.vse.java.listingsapp.service;
    exports cz.vse.java.listingsapp;
    exports cz.vse.java.listingsapp.controller.listingControllers;
    opens cz.vse.java.listingsapp.controller.listingControllers to javafx.fxml;
    exports cz.vse.java.listingsapp.controller.offerControllers;
    opens cz.vse.java.listingsapp.controller.offerControllers to javafx.fxml;
    exports cz.vse.java.listingsapp.controller.singUp;
    opens cz.vse.java.listingsapp.controller.singUp to javafx.fxml;
    exports cz.vse.java.listingsapp.controller.login;
    opens cz.vse.java.listingsapp.controller.login to javafx.fxml;
    exports cz.vse.java.listingsapp.controller.business;
    opens cz.vse.java.listingsapp.controller.business to javafx.fxml;
}
