package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.PersonalRequest;
import org.oyakushev.hospitalclient.dto.PersonalResponse;
import org.oyakushev.hospitalclient.dto.PersonalRole;

public class PersonalController {

    public TextField usernameField;
    public PasswordField passwordField;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField phoneField;
    public TextArea descriptionArea;
    public ComboBox<String> roleCombo;
    public Button saveButton;
    public Button cancelButton;
    public Label idLabel;

    public void initialize() {
        roleCombo.setItems(FXCollections.observableArrayList(
                PersonalRole.Viewer.name(),
                PersonalRole.Nurse.name(),
                PersonalRole.Doctor.name(),
                PersonalRole.Admin.name()
        ));

        loadData();
    }

    private void setControlsDisable(boolean isDisable) {
        saveButton.setDisable(isDisable);
        cancelButton.setDisable(isDisable);
    }

    private void loadData() {
        setControlsDisable(true);
        Task<PersonalResponse> loadDataTask = new Task<>() {

            @Override
            protected PersonalResponse call() throws Exception {
                Long id = HospitalApplication.getInstance().getPersonalId();
                return HospitalApplication.getInstance().getPersonalApiService().getById(id);
            }

            @Override
            protected void succeeded() {
                PersonalResponse personalResponse = getValue();
                idLabel.setText("#" + personalResponse.getId());
                usernameField.setText(personalResponse.getUsername());
                firstNameField.setText(personalResponse.getFirstName());
                lastNameField.setText(personalResponse.getLastName());
                phoneField.setText(personalResponse.getPhone());
                descriptionArea.setText(personalResponse.getDescription());
                roleCombo.getSelectionModel().select(PersonalRole.fromIndex(personalResponse.getRole()).name());
                setControlsDisable(false);
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to load personal: " + getException().getMessage());
            }
        };

        new Thread(loadDataTask).start();
    }

    private void saveData() {
        setControlsDisable(true);
        Task<PersonalResponse> saveDataTask = new Task<>() {

            @Override
            protected PersonalResponse call() throws Exception {
                Long id = HospitalApplication.getInstance().getPersonalId();

                PersonalRequest personalRequest = new PersonalRequest();
                personalRequest.setUsername(usernameField.getText());
                if (!passwordField.getText().isEmpty()) {
                    personalRequest.setPassword(passwordField.getText());
                }
                personalRequest.setFirstName(firstNameField.getText());
                personalRequest.setLastName(lastNameField.getText());
                personalRequest.setPhone(phoneField.getText());
                personalRequest.setDescription(descriptionArea.getText());
                PersonalRole personalRole = PersonalRole.valueOf(roleCombo.getValue());
                personalRequest.setRole(personalRole.getIndex());

                return HospitalApplication.getInstance().getPersonalApiService().update(id, personalRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to save personal: " + getException().getMessage());
            }
        };

        new Thread(saveDataTask).start();
    }

    private boolean validateData() {
        if (usernameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Username can't be empty.");
            return false;
        }

        if (firstNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("First name can't be empty.");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Last name can't be empty.");
            return false;
        }

        String phoneValue = phoneField.getText();
        if (phoneValue.isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Phone can't be empty.");
            return false;
        }

        for (int i=0;i<phoneValue.length();i++) {
            if (!Character.isDigit(phoneValue.charAt(i))) {
                HospitalApplication.getInstance().showAlertWarning("Phone should contains digits only.");
                return false;
            }
        }

        return true;
    }

    public void onSaveAction(ActionEvent actionEvent) {
        if (validateData()) {
            saveData();
        }
    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
