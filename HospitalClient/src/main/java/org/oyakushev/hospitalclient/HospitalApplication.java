package org.oyakushev.hospitalclient;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HospitalApplication extends Application {
    private Stage stage;

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

    public void gotoPatientWindow() {
        replaceSceneContent("patient.fxml");
    }

    public void gotoPersonalWindow() {
        replaceSceneContent("personal.fxml");
    }
}
