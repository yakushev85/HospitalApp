package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.BloodRequest;
import org.oyakushev.hospitalclient.dto.BloodResponse;

public class NewBloodController {
    public TextField hemoglobinField;
    public TextField hematocritField;
    public TextField mcvField;
    public TextField mchField;
    public Button saveButton;
    public Button cancelButton;

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    private boolean validateData() {
        if (hemoglobinField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Hemoglobin can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(hemoglobinField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Hemoglobin must be a number.");
            return false;
        }

        if (hematocritField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Hematocrit can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(hematocritField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Hematocrit must be a number.");
            return false;
        }

        if (mcvField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Mean Corpuscular Volume can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(mcvField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Mean Corpuscular Volume must be a number.");
            return false;
        }

        if (mchField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Mean Corpuscular Hemoglobin can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(mchField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Mean Corpuscular Hemoglobin must be a number.");
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

        Task<BloodResponse> dataTask = new Task<>() {

            @Override
            protected BloodResponse call() throws Exception {
                BloodRequest bloodRequest = new BloodRequest();
                bloodRequest.setHematocrit(Double.parseDouble(hematocritField.getText()));
                bloodRequest.setHemoglobin(Double.parseDouble(hemoglobinField.getText()));
                bloodRequest.setMeanCorpuscularVolume(Double.parseDouble(mcvField.getText()));
                bloodRequest.setMeanCorpuscularHemoglobin(Double.parseDouble(mchField.getText()));
                bloodRequest.setPatientId(HospitalApplication.getInstance().getPatientId());

                return ApiClient.Instance.createBlood(bloodRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoPatientWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create blood record: " + getException().getMessage());
            }
        };

        new Thread(dataTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }
}
