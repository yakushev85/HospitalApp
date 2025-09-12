package org.oyakushev.hospitalclient.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.AuthRequest;
import org.oyakushev.hospitalclient.dto.AuthResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LoginController {
    public TextField loginUsername;
    public PasswordField loginPassword;
    public Button loginButton;

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
                return ApiClient.Instance.loginUser(authRequest).orElseThrow();
            }

            @Override
            protected void succeeded() {
                loginButton.setDisable(false);

                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                System.err.println("Failed to login: " + getException().getMessage());
                loginButton.setDisable(false);
            }
        };

        new Thread(loginTask).start();
    }
}
