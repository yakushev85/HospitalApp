package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.PersonalRequest;
import org.oyakushev.hospitalclient.dto.PersonalResponse;
import org.oyakushev.hospitalclient.dto.PersonalRole;

public class NewPersonalController {
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField phoneField;
    public TextArea descriptionArea;
    public ComboBox<String> roleCombo;
    public Button saveButton;
    public Button cancelButton;

    public void initialize() {
        ObservableList<String> roleList = FXCollections.observableArrayList(
                PersonalRole.Viewer.getTitle(),
                PersonalRole.Nurse.getTitle(),
                PersonalRole.Doctor.getTitle(),
                PersonalRole.Admin.getTitle()
        );
        roleCombo.setItems(roleList);
        roleCombo.setValue(PersonalRole.Viewer.getTitle());
    }

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    public void onSaveAction(ActionEvent actionEvent) {
        setControlsDisable(true);

        final PersonalRequest personalRequest = new PersonalRequest();
        personalRequest.setUsername(usernameField.getText());
        personalRequest.setPassword(passwordField.getText());
        personalRequest.setFirstName(firstNameField.getText());
        personalRequest.setLastName(lastNameField.getText());
        personalRequest.setPhone(phoneField.getText());
        personalRequest.setDescription(descriptionArea.getText());
        personalRequest.setRole(2);

        Task<PersonalResponse> savePersonalTask = new Task<> () {
            @Override
            protected PersonalResponse call() throws Exception {
                return ApiClient.Instance.createPersonal(personalRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to create personal: " + getException().getMessage());
            }
        };

        new Thread(savePersonalTask).start();
    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
