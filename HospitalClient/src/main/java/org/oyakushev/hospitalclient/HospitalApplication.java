package org.oyakushev.hospitalclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.oyakushev.hospitalclient.dto.PersonalRole;

import java.io.IOException;

public class HospitalApplication extends Application {
    private Stage stage;

    private String username;
    private PersonalRole personalRole;
    private Long patientId;
    private Long personalId;

    private static HospitalApplication instance;

    public HospitalApplication() {
        instance = this;
    }

    public static HospitalApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        gotoLoginWindow();
    }

    public PersonalRole getPersonalRole() {
        return personalRole;
    }

    public void setPersonalRole(PersonalRole personalRole) {
        this.personalRole = personalRole;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }

    private void replaceSceneContent(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HospitalApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), 800, 450);
            stage.setTitle("Hospital");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void gotoLoginWindow() {
        replaceSceneContent("login.fxml");
    }

    public void gotoHomeWindow() {
        replaceSceneContent("home.fxml");
    }

    public void gotoNewPatientWindow() {
        replaceSceneContent("new_patient.fxml");
    }

    public void gotoNewPersonalWindow() {
        replaceSceneContent("new_personal.fxml");
    }

    public void gotoPatientWindow() {
        replaceSceneContent("patient.fxml");
    }

    public void gotoPersonalWindow() {
        replaceSceneContent("personal.fxml");
    }

    public void gotoChangePassWindow() {
        replaceSceneContent("change_pass.fxml");
    }

}
