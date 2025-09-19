package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.PagePatients;
import org.oyakushev.hospitalclient.dto.PatientResponse;

public class HomeController {
    private int pageNumber = 1;
    private int pageSize = 10;

    // Patient tab
    public TextField searchPatientField;
    public Button searchPatientButton;
    public ComboBox searchPatientByCombo;
    public Button newPatientButton;
    public TableView<PatientResponse> patientTable;

    //Personal tab
    public TextField searchPersonalField;
    public Button searchPersonalButton;
    public ComboBox searchPersonalByCombo;
    public Button newPersonalButton;
    public TableView personalTable;

    public void initialize() {
        loadPatients();
    }

    private void loadPatients() {
        Task<PagePatients> loadPatientsTask = new Task<PagePatients>() {
            @Override
            protected PagePatients call() throws Exception {
                return ApiClient.Instance.getPatients(pageNumber-1, pageSize).orElseThrow();
            }

            @Override
            protected void succeeded() {
                ObservableList<PatientResponse> data = FXCollections.observableArrayList(getValue().getContent());
                patientTable.setItems(data);
            }

            @Override
            protected void failed() {
                System.err.println("Failed to load patients: " + getException().getMessage());
            }
        };

        new Thread(loadPatientsTask).start();
    }

    public void onActionNewPatientButton(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPatientWindow();
    }

    public void onActionNewPersonalButton(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoPersonalWindow();
    }
}
