module org.oyakushev.hospital.client.hospitalclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.oyakushev.hospitalclient to javafx.fxml;
    exports org.oyakushev.hospitalclient;
}