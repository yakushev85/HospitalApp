package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.VaccinationRequest;
import org.oyakushev.hospitalclient.dto.VaccinationResponse;

public class NewVaccinationController {
    public TextArea descriptionArea;
    public TextField effectiveTimeField;
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

        if (effectiveTimeField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Effective time can't be empty.");
            return false;
        }

        try {
            Integer.parseInt(effectiveTimeField.getText());
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

        Task<VaccinationResponse> dataTask = new Task<>() {

            @Override
            protected VaccinationResponse call() throws Exception {
                VaccinationRequest vaccinationRequest = new VaccinationRequest();
                vaccinationRequest.setDescription(descriptionArea.getText());
                vaccinationRequest.setEffectiveTime(Integer.parseInt(effectiveTimeField.getText()));
                vaccinationRequest.setPatientId(HospitalApplication.getInstance().getPatientId());

                return HospitalApplication.getInstance().getVaccinationApiService().create(vaccinationRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoPatientWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create vaccination record: " + getException().getMessage());
            }
        };

        new Thread(dataTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }
}
