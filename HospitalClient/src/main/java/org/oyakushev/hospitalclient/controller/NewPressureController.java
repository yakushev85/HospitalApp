package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.PressureRequest;
import org.oyakushev.hospitalclient.dto.PressureResponse;

public class NewPressureController {
    public TextField systolicField;
    public TextField diastolicField;
    public Button saveButton;
    public Button cancelButton;

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    private boolean validateData() {
        if (systolicField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Systolic can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(systolicField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Systolic must be a number.");
            return false;
        }

        if (diastolicField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Diastolic can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(diastolicField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Diastolic must be a number.");
            return false;
        }

        return true;
    }

    public void onSave(ActionEvent actionEvent) {
        if (validateData()) {
            saveData();
        }
    }

    private void saveData() {
        setControlsDisable(true);

        Task<PressureResponse> dataTask = new Task<>() {

            @Override
            protected PressureResponse call() throws Exception {
                PressureRequest pressureRequest = new PressureRequest();
                pressureRequest.setSystolic(Double.parseDouble(systolicField.getText()));
                pressureRequest.setDiastolic(Double.parseDouble(diastolicField.getText()));
                pressureRequest.setPatientId(HospitalApplication.getInstance().getPatientId());

                return HospitalApplication.getInstance().getPressureApiService().create(pressureRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoPatientWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create pressure record: " + getException().getMessage());
            }
        };

        new Thread(dataTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }
}
