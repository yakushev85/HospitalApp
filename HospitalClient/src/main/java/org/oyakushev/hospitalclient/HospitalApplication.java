package org.oyakushev.hospitalclient;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.oyakushev.hospitalclient.api.*;
import org.oyakushev.hospitalclient.dto.PersonalRole;

import java.io.IOException;

public class HospitalApplication extends Application {
    private Stage stage;

    private final Gson gson;

    private String username;
    private PersonalRole personalRole;
    private Long patientId;
    private Long personalId;

    private final AuthApiService authApiService;
    private final BloodApiService bloodApiService;
    private final ExaminationApiService examinationApiService;
    private final MedicalTestApiService medicalTestApiService;
    private final PatientApiService patientApiService;
    private final PersonalApiService personalApiService;
    private final PressureApiService pressureApiService;
    private final VaccinationApiService vaccinationApiService;

    private static HospitalApplication instance;

    public HospitalApplication() {
        instance = this;
        gson = new Gson();

        authApiService = new AuthApiService();
        bloodApiService = new BloodApiService();
        examinationApiService = new ExaminationApiService();
        medicalTestApiService = new MedicalTestApiService();
        patientApiService = new PatientApiService();
        personalApiService = new PersonalApiService();
        pressureApiService = new PressureApiService();
        vaccinationApiService = new VaccinationApiService();
    }

    public static HospitalApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        gotoLoginWindow();
    }

    public Gson getGson() {
        return gson;
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

    public void showAlertWarning(String msg) {
        Alert alertWindow = new Alert(Alert.AlertType.WARNING,  msg, ButtonType.OK);
        alertWindow.show();
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

    public void gotoNewBloodWindow() {
        replaceSceneContent("new_blood.fxml");
    }

    public void gotoNewExaminationWindow() {
        replaceSceneContent("new_examination.fxml");
    }

    public void gotoNewMedicalTestWindow() {
        replaceSceneContent("new_medical_test.fxml");
    }

    public void gotoNewPressureWindow() {
        replaceSceneContent("new_pressure.fxml");
    }

    public void gotoNewVaccinationWindow() {
        replaceSceneContent("new_vaccination.fxml");
    }

    public AuthApiService getAuthApiService() {
        return authApiService;
    }

    public BloodApiService getBloodApiService() {
        return bloodApiService;
    }

    public ExaminationApiService getExaminationApiService() {
        return examinationApiService;
    }

    public MedicalTestApiService getMedicalTestApiService() {
        return medicalTestApiService;
    }

    public PatientApiService getPatientApiService() {
        return patientApiService;
    }

    public PersonalApiService getPersonalApiService() {
        return personalApiService;
    }

    public PressureApiService getPressureApiService() {
        return pressureApiService;
    }

    public VaccinationApiService getVaccinationApiService() {
        return vaccinationApiService;
    }
}
