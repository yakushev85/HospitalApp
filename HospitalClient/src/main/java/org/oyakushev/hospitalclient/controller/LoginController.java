package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.AuthRequest;
import org.oyakushev.hospitalclient.dto.AuthResponse;
import org.oyakushev.hospitalclient.dto.MessageResponse;
import org.oyakushev.hospitalclient.dto.PersonalRole;

public class LoginController {
    public TextField loginUsername;
    public PasswordField loginPassword;
    public Button loginButton;
    public Label errorText;

    public void initialize() {
        loginButton.setDisable(true);
        checkStatus();
    }

    public void onLoginButtonPressed(ActionEvent actionEvent) {
        loginButton.setDisable(true);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(loginUsername.getText());
        authRequest.setPassword(loginPassword.getText());

        loginUser(authRequest);
    }

    private void loginUser(AuthRequest authRequest) {
        Task<AuthResponse> loginTask = new Task<>() {
            @Override
            protected AuthResponse call() throws Exception {
                return HospitalApplication.getInstance().getAuthApiService().loginUser(authRequest);
            }

            @Override
            protected void succeeded() {
                loginButton.setDisable(false);
                String userRole = getValue().getRole();

                HospitalApplication.getInstance().setPersonalRole(PersonalRole.valueOf(userRole));
                HospitalApplication.getInstance().setUsername(getValue().getUsername());
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                errorText.setText(getException().getMessage());
                loginButton.setDisable(false);
            }
        };

        new Thread(loginTask).start();
    }

    private void checkStatus() {
        Task<MessageResponse> checkStatusTask = new Task<>() {
            @Override
            protected MessageResponse call() throws Exception {
                return HospitalApplication.getInstance().getAuthApiService().getStatus();
            }

            @Override
            protected void succeeded() {
                loginButton.setDisable(false);
                loginButton.setDefaultButton(true);
            }

            @Override
            protected void failed() {
                errorText.setText("Server is not accessible.");
            }
        };

        new Thread(checkStatusTask).start();
    }
}
