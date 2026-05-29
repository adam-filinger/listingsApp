module cz.vse.java.listingsapp.listingsapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;

    opens cz.vse.java.listingsapp.listingsapp to javafx.fxml;
    opens cz.vse.java.listingsapp.controller to javafx.fxml;

    exports cz.vse.java.listingsapp.controller to javafx.fxml, javafx.graphics;

}

