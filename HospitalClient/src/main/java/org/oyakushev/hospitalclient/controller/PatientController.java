package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.PatientRequest;
import org.oyakushev.hospitalclient.dto.PatientResponse;

import java.time.format.DateTimeFormatter;

public class PatientController {

    public TextField firstNameField;
    public TextField lastNameField;
    public DatePicker birthdayPicker;
    public TextField addressField;
    public TextField heightField;
    public TextField weightField;
    public Button saveButton;
    public Button cancelButton;

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    public void onSaveAction(ActionEvent actionEvent) {
        setControlsDisable(true);

        final PatientRequest patientRequest = new PatientRequest();
        patientRequest.setFirstName(firstNameField.getText());
        patientRequest.setLastName(lastNameField.getText());
        patientRequest.setDob(birthdayPicker.getValue().format(DateTimeFormatter.ISO_DATE));
        patientRequest.setAddress(addressField.getText());
        patientRequest.setHeight(Double.parseDouble(heightField.getText()));
        patientRequest.setWeight(Double.parseDouble(weightField.getText()));

        Task<PatientResponse> savePatientTask = new Task<>() {
            @Override
            protected PatientResponse call() throws Exception {
                return ApiClient.Instance.createPatient(patientRequest).orElseThrow();
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create patient: " + getException().getMessage());
            }
        };

        new Thread(savePatientTask).start();
    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
