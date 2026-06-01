module cz.vse.java.listingsapp.listingsapp {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires ch.qos.logback.classic;
    requires org.slf4j;

    opens cz.vse.java.listingsapp.model to org.hibernate.orm.core;
    opens cz.vse.java.listingsapp.controller to javafx.fxml;

    exports cz.vse.java.listingsapp.controller;
    exports cz.vse.java.listingsapp.model;
    exports cz.vse.java.listingsapp.service;
    exports cz.vse.java.listingsapp;
}
