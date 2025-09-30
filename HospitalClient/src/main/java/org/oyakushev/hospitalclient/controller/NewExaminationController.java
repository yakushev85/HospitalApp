package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.ExaminationRequest;
import org.oyakushev.hospitalclient.dto.ExaminationResponse;

public class NewExaminationController {
    public TextArea descriptionArea;
    public TextArea diagnosisArea;
    public Button saveButton;
    public Button cancelButton;

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    private boolean validateData() {
        if (descriptionArea.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Description can't be empty.");
            return false;
        }

        if (diagnosisArea.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Diagnosis can't be empty.");
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

        Task<ExaminationResponse> dataTask = new Task<>() {

            @Override
            protected ExaminationResponse call() throws Exception {
                ExaminationRequest examinationRequest = new ExaminationRequest();
                examinationRequest.setDescription(descriptionArea.getText());
                examinationRequest.setDiagnosis(diagnosisArea.getText());
                examinationRequest.setPatientId(HospitalApplication.getInstance().getPatientId());

                return ApiClient.Instance.createExamination(examinationRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoPatientWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create examination record: " + getException().getMessage());
            }
        };

        new Thread(dataTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }
}
