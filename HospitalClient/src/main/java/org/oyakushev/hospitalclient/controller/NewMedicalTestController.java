package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.MedicalTestRequest;
import org.oyakushev.hospitalclient.dto.MedicalTestResponse;

public class NewMedicalTestController {
    public TextArea descriptionArea;
    public ComboBox<String> resultCombo;
    public Button saveButton;
    public Button cancelButton;

    public void initialize() {
        resultCombo.setItems(FXCollections.observableArrayList("Unknown", "Negative", "Positive"));
    }

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    private boolean validateData() {
        if (descriptionArea.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Description can't be empty.");
            return false;
        }

        if (resultCombo.getSelectionModel().getSelectedItem().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Result can't be empty.");
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

        Task<MedicalTestResponse> dataTask = new Task<>() {

            @Override
            protected MedicalTestResponse call() throws Exception {
                MedicalTestRequest medicalTestRequest = new MedicalTestRequest();
                medicalTestRequest.setDescription(descriptionArea.getText());
                switch (resultCombo.getSelectionModel().getSelectedItem()) {
                    case "Unknown": medicalTestRequest.setResult(0); break;
                    case "Negative": medicalTestRequest.setResult(-1); break;
                    case "Positive": medicalTestRequest.setResult(1); break;
                }
                medicalTestRequest.setPatientId(HospitalApplication.getInstance().getPatientId());

                return ApiClient.Instance.createMedicalTest(medicalTestRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoPatientWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create medical test record: " + getException().getMessage());
            }
        };

        new Thread(dataTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }
}

