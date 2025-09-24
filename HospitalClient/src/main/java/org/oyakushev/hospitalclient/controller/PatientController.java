package org.oyakushev.hospitalclient.controller;

import javafx.event.ActionEvent;
import org.oyakushev.hospitalclient.HospitalApplication;

public class PatientController {
    public void onSaveAction(ActionEvent actionEvent) {

    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
