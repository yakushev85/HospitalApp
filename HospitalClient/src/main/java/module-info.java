module org.oyakushev.hospitalclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;
    requires javafx.base;

    opens org.oyakushev.hospitalclient to javafx.fxml;
    exports org.oyakushev.hospitalclient;
    exports org.oyakushev.hospitalclient.controller;
    opens org.oyakushev.hospitalclient.controller to javafx.fxml;
    opens org.oyakushev.hospitalclient.dto to com.google.gson, javafx.base;
}