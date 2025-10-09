package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.ChangePasswordRequest;
import org.oyakushev.hospitalclient.dto.MessageResponse;

public class ChangePassController {
    public PasswordField oldPasswordField;
    public PasswordField newPasswordField;
    public PasswordField reNewPasswordField;
    public Button saveButton;
    public Button cancelButton;

    public void onSave(ActionEvent actionEvent) {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String reNewPassword = reNewPasswordField.getText();

        if (oldPassword.isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Old password can't be empty.");
            return;
        }

        if (newPassword.isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("New password can't be empty.");
            return;
        }

        if (oldPassword.equals(newPassword)) {
            HospitalApplication.getInstance().showAlertWarning("New password can't be the same as old password.");
            return;
        }

        if (!newPassword.equals(reNewPassword)) {
            HospitalApplication.getInstance().showAlertWarning("Please repeat new password correctly.");
            return;
        }

        saveButton.setDisable(true);
        cancelButton.setDisable(true);

        saveNewPassword(oldPassword, newPassword);
    }

    private void saveNewPassword(String oldPassword, String newPassword) {
        Task<MessageResponse> changePasswordTask = new Task<>() {

            @Override
            protected MessageResponse call() throws Exception {
                ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
                changePasswordRequest.setNewPassword(newPassword);
                changePasswordRequest.setOldPassword(oldPassword);

                return HospitalApplication.getInstance().getAuthApiService().changePassword(changePasswordRequest);
            }

            @Override
            protected void succeeded() {
                saveButton.setDisable(false);
                cancelButton.setDisable(false);

                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                System.err.println("Failed to load personal: " + getException().getMessage());
                saveButton.setDisable(false);
                cancelButton.setDisable(false);
            }
        };

        new Thread(changePasswordTask).start();
    }

    public void onCancel(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }
}
