package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.PatientRequest;
import org.oyakushev.hospitalclient.dto.PatientResponse;
import org.oyakushev.hospitalclient.dto.PersonalRole;

import java.time.LocalDate;
import java.time.ZoneId;
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
    public Label idLabel;

    public void initialize() {
        checkRole(HospitalApplication.getInstance().getPersonalRole());

        loadGeneralData();
    }

    private void loadGeneralData() {
        setControlsDisable(true);
        Task<PatientResponse> loadDataTask = new Task<>() {

            @Override
            protected PatientResponse call() throws Exception {
                Long id = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getPatient(id);
            }

            @Override
            protected void succeeded() {
                PatientResponse patientResponse = getValue();
                idLabel.setText("#" + patientResponse.getId());
                firstNameField.setText(patientResponse.getFirstName());
                lastNameField.setText(patientResponse.getLastName());
                LocalDate dobLocalValue = patientResponse.getDob()
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                birthdayPicker.setValue(dobLocalValue);
                addressField.setText(patientResponse.getAddress());
                heightField.setText(String.valueOf(patientResponse.getHeight()));
                weightField.setText(String.valueOf(patientResponse.getWeight()));

                setControlsDisable(false);
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to load patient: " + getException().getMessage());
            }
        };

        new Thread(loadDataTask).start();
    }

    private void checkRole(PersonalRole personalRole) {
        if (personalRole.getIndex() < PersonalRole.Nurse.getIndex()) {
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            birthdayPicker.setDisable(true);
            addressField.setDisable(true);
            heightField.setDisable(true);
            weightField.setDisable(true);
            saveButton.setDisable(true);
        } else {
            firstNameField.setDisable(false);
            lastNameField.setDisable(false);
            birthdayPicker.setDisable(false);
            addressField.setDisable(false);
            heightField.setDisable(false);
            weightField.setDisable(false);
            saveButton.setDisable(false);
        }
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

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    public void onSaveAction(ActionEvent actionEvent) {
        if (!validateData()) {
            return;
        }

        updateGeneralData();
    }

    private void updateGeneralData() {
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
                Long id = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.updatePatient(id, patientRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to update patient: " + getException().getMessage());
            }
        };

        new Thread(savePatientTask).start();
    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
