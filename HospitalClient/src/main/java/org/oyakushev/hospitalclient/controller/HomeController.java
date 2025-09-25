package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.*;

public class HomeController {
    public Button logoutButton;

    // Patient tab
    public TextField searchPatientField;
    public Button searchPatientButton;
    public Button showPatientButton;
    public Button newPatientButton;
    public TableView<PatientResponse> patientTable;
    public Label patientsPageLabel;

    //Personal tab
    public Tab personalTab;
    public TextField searchPersonalField;
    public Button searchPersonalButton;
    public Button showPersonalButton;
    public Button newPersonalButton;
    public TableView<PersonalResponse> personalTable;
    public Label personalPageLabel;

    public Label profileUsername;
    public Label profileRole;

    private int pagePatientsNumber = 1;
    private int pagePatientsSize = 10;
    private int totalPatientsPages = 1;

    private int pagePersonalNumber = 1;
    private int pagePersonalSize = 10;
    private int totalPersonalPages = 1;


    public void initialize() {
        loadPatients();

        if (HospitalApplication.getInstance().getPersonalRole().equals(PersonalRole.Admin)) {
            personalTab.setDisable(false);
            loadPersonal();
        } else {
            personalTab.setDisable(true);
        }

        profileRole.setText("Role: " + HospitalApplication.getInstance().getPersonalRole());
        profileUsername.setText(HospitalApplication.getInstance().getUsername());
    }

    private void loadPersonal() {
        Task<PagePersonal> loadPersonalTask = new Task<>() {

            @Override
            protected PagePersonal call() throws Exception {
                return ApiClient.Instance.getPersonal(pagePersonalNumber - 1, pagePersonalSize);
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
                return ApiClient.Instance.getPatients(pagePatientsNumber -1, pagePatientsSize);
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
        HospitalApplication.getInstance().gotoNewPatientWindow();
    }

    public void onActionNewPersonalButton(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewPersonalWindow();
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

    public void onLogout(ActionEvent actionEvent) {
        logout();
    }

    private void logout() {
        Task<MessageResponse> logoutTask = new Task<> () {

            @Override
            protected MessageResponse call() throws Exception {
                return ApiClient.Instance.logout();
            }

            @Override
            protected void succeeded() {
                HospitalApplication.getInstance().gotoLoginWindow();
            }

            @Override
            protected void failed() {
                System.err.println("Failed to load patients: " + getException().getMessage());
                HospitalApplication.getInstance().gotoLoginWindow();
            }
        };

        new Thread(logoutTask).start();
    }

    public void onChangePassword(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoChangePassWindow();
    }

    public void onPatientShow(ActionEvent actionEvent) {
        Long patientId = patientTable.getSelectionModel().getSelectedItem().getId();
        HospitalApplication.getInstance().setPatientId(patientId);
        HospitalApplication.getInstance().gotoPatientWindow();
    }

    public void onPersonalShow(ActionEvent actionEvent) {
        Long personalId = personalTable.getSelectionModel().getSelectedItem().getId();
        HospitalApplication.getInstance().setPersonalId(personalId);
        HospitalApplication.getInstance().gotoPersonalWindow();
    }
}
