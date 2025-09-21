package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.PagePatients;
import org.oyakushev.hospitalclient.dto.PagePersonal;
import org.oyakushev.hospitalclient.dto.PatientResponse;
import org.oyakushev.hospitalclient.dto.PersonalResponse;

public class HomeController {
    private int pagePatientsNumber = 1;
    private int pagePatientsSize = 10;
    private int totalPatientsPages = 1;

    private int pagePersonalNumber = 1;
    private int pagePersonalSize = 10;
    private int totalPersonalPages = 1;

    // Patient tab
    public TextField searchPatientField;
    public Button searchPatientButton;
    public ComboBox searchPatientByCombo;
    public Button newPatientButton;
    public TableView<PatientResponse> patientTable;
    public Label patientsPageLabel;

    //Personal tab
    public Tab personalTab;
    public TextField searchPersonalField;
    public Button searchPersonalButton;
    public ComboBox searchPersonalByCombo;
    public Button newPersonalButton;
    public TableView<PersonalResponse> personalTable;
    public Label personalPageLabel;

    public void initialize() {
        loadPatients();
        loadPersonal();
    }

    private void loadPersonal() {
        Task<PagePersonal> loadPersonalTask = new Task<>() {

            @Override
            protected PagePersonal call() throws Exception {
                return ApiClient.Instance.getPersonal(pagePersonalNumber - 1, pagePersonalSize).orElseThrow();
            }

            @Override
            protected void succeeded() {
                ObservableList<PersonalResponse> data = FXCollections.observableArrayList(getValue().getContent());
                personalTable.setItems(data);
                updatePersonalPages(getValue());
            }

            @Override
            protected void failed() {
                System.err.println("Failed to load personal: " + getException().getMessage());
            }
        };

        new Thread(loadPersonalTask).start();
    }

    private void loadPatients() {
        Task<PagePatients> loadPatientsTask = new Task<PagePatients>() {
            @Override
            protected PagePatients call() throws Exception {
                return ApiClient.Instance.getPatients(pagePatientsNumber -1, pagePatientsSize).orElseThrow();
            }

            @Override
            protected void succeeded() {
                ObservableList<PatientResponse> data = FXCollections.observableArrayList(getValue().getContent());
                patientTable.setItems(data);
                updatePatientPages(getValue());
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

    private void updatePatientPages(PagePatients pagePatients) {
        pagePatientsNumber = pagePatients.getNumber() + 1;
        totalPatientsPages = pagePatients.getTotalPages();

        patientsPageLabel.setText(pagePatientsNumber + " of " + totalPatientsPages + " page");
    }

    private void updatePersonalPages(PagePersonal pagePersonal) {
        pagePersonalNumber = pagePersonal.getNumber() + 1;
        totalPersonalPages = pagePersonal.getTotalPages();

        personalPageLabel.setText(pagePersonalNumber + " of " + totalPersonalPages + " page");
    }

}
