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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewPatientController {

    public TextField firstNameField;
    public TextField lastNameField;
    public DatePicker birthdayPicker;
    public TextField addressField;
    public TextField heightField;
    public TextField weightField;
    public Button saveButton;
    public Button cancelButton;

    public void initialize() {
        birthdayPicker.setValue(LocalDate.now());
    }

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    private boolean validateData() {
        if (firstNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("First name can't be empty.");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Last name can't be empty.");
            return false;
        }

        if (addressField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Address can't be empty.");
            return false;
        }

        if (heightField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Height can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(heightField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Height must be a number.");
            return false;
        }

        if (weightField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Weight can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(weightField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Weight must be a number.");
            return false;
        }

        return true;
    }

    public void onSaveAction(ActionEvent actionEvent) {
        if (!validateData()) {
            return;
        }

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
                return ApiClient.Instance.createPatient(patientRequest);
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
