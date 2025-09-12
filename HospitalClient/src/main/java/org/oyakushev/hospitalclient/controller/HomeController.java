package org.oyakushev.hospitalclient.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;

public class HomeController {

    // Patient tab
    public TextField searchPatientField;
    public Button searchPatientButton;
    public ComboBox searchPatientByCombo;
    public Button newPatientButton;
    public TableView patientTable;

    //Personal tab
    public TextField searchPersonalField;
    public Button searchPersonalButton;
    public ComboBox searchPersonalByCombo;
    public Button newPersonalButton;
    public TableView personalTable;

    public void onActionNewPatientButton(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }

    public void onActionNewPersonalButton(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPersonalWindow();
    }
}
