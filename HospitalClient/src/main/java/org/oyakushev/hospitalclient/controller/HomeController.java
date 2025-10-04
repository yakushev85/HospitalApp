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
    public Button changePasswordButton;

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

    public Button firstPersonalPageButton;
    public Button previousPersonalPageButton;
    public Button nextPersonalPageButton;
    public Button lastPersonalPageButton;

    public Button firstPatientPageButton;
    public Button previousPatientPageButton;
    public Button nextPatientPageButton;
    public Button lastPatientPageButton;
    public ComboBox<Integer> patientPageSizeCombo;
    public ComboBox<Integer> personalPageSizeCombo;

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

        patientPageSizeCombo.setItems(FXCollections.observableArrayList(10, 15, 20, 25, 30));
        patientPageSizeCombo.setValue(pagePatientsSize);

        personalPageSizeCombo.setItems(FXCollections.observableArrayList(10, 15, 20, 25, 30));
        personalPageSizeCombo.setValue(pagePersonalSize);
    }

    private void setControlsDisable(boolean isDisable) {
        searchPatientButton.setDisable(isDisable);
        showPatientButton.setDisable(isDisable);
        newPersonalButton.setDisable((isDisable));

        searchPersonalButton.setDisable(isDisable);
        showPersonalButton.setDisable(isDisable);
        newPersonalButton.setDisable(isDisable);

        firstPersonalPageButton.setDisable(isDisable);
        previousPersonalPageButton.setDisable(isDisable);
        nextPersonalPageButton.setDisable(isDisable);
        lastPersonalPageButton.setDisable(isDisable);

        firstPatientPageButton.setDisable(isDisable);
        previousPatientPageButton.setDisable(isDisable);
        nextPatientPageButton.setDisable(isDisable);
        lastPatientPageButton.setDisable(isDisable);

        logoutButton.setDisable(isDisable);
        changePasswordButton.setDisable(isDisable);
    }

    private void loadPersonal() {
        setControlsDisable(true);
        Task<PagePersonal> loadPersonalTask = new Task<>() {

            @Override
            protected PagePersonal call() throws Exception {
                return ApiClient.Instance.getPersonal(pagePersonalNumber - 1, pagePersonalSize);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                ObservableList<PersonalResponse> data = FXCollections.observableArrayList(getValue().getContent());
                personalTable.setItems(data);
                updatePersonalPages(getValue());
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to load personal: " + getException().getMessage());
            }
        };

        new Thread(loadPersonalTask).start();
    }

    private void loadPatients() {
        setControlsDisable(true);
        Task<PagePatients> loadPatientsTask = new Task<PagePatients>() {
            @Override
            protected PagePatients call() throws Exception {
                return ApiClient.Instance.getPatients(pagePatientsNumber -1, pagePatientsSize);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                ObservableList<PatientResponse> data = FXCollections.observableArrayList(getValue().getContent());
                patientTable.setItems(data);
                updatePatientPages(getValue());
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
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

        updatePatientPageLabels();
    }

    private void updatePersonalPages(PagePersonal pagePersonal) {
        pagePersonalNumber = pagePersonal.getNumber() + 1;
        totalPersonalPages = pagePersonal.getTotalPages();

        updatePersonalPageLabels();
    }

    private void updatePatientPageLabels() {
        patientsPageLabel.setText(pagePatientsNumber + " of " + totalPatientsPages + " page");
    }

    private void updatePersonalPageLabels() {
        personalPageLabel.setText(pagePersonalNumber + " of " + totalPersonalPages + " page");
    }

    public void onLogout(ActionEvent actionEvent) {
        logout();
    }

    private void logout() {
        setControlsDisable(true);
        Task<MessageResponse> logoutTask = new Task<> () {

            @Override
            protected MessageResponse call() throws Exception {
                return ApiClient.Instance.logout();
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoLoginWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
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
        PatientResponse selectedItem = patientTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        Long patientId = selectedItem.getId();
        HospitalApplication.getInstance().setPatientId(patientId);
        HospitalApplication.getInstance().gotoPatientWindow();
    }

    public void onPersonalShow(ActionEvent actionEvent) {
        PersonalResponse selectedItem = personalTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        Long personalId = selectedItem.getId();
        HospitalApplication.getInstance().setPersonalId(personalId);
        HospitalApplication.getInstance().gotoPersonalWindow();
    }

    public void onSearchPatients(ActionEvent actionEvent) {
        String searchText = searchPatientField.getText();
        searchPatients(searchText);
    }

    private void searchPatients(String searchText) {
        if (searchText.isEmpty()) {
            loadPatients();
            return;
        }

        setControlsDisable(true);
        Task<SearchPatientResultsResponse> searchTask = new Task<> () {

            @Override
            protected SearchPatientResultsResponse call() throws Exception {
                return ApiClient.Instance.searchPatients(searchText);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                ObservableList<PatientResponse> data = FXCollections.observableArrayList(getValue().getResults());
                patientTable.setItems(data);
                PagePatients pagePatients = new PagePatients();
                pagePatients.setTotalPages(1);
                pagePatients.setNumber(0);
                updatePatientPages(pagePatients);
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to search patients: " + getException().getMessage());
            }
        };

        new Thread(searchTask).start();
    }

    public void onSearchPersonal(ActionEvent actionEvent) {
        String searchText = searchPersonalField.getText();
        searchPersonal(searchText);
    }

    private void searchPersonal(String searchText) {
        if (searchText.isEmpty()) {
            loadPersonal();
            return;
        }

        setControlsDisable(true);
        Task<SearchPersonalResultsResponse> searchTask = new Task<> () {

            @Override
            protected SearchPersonalResultsResponse call() throws Exception {
                return ApiClient.Instance.searchPersonal(searchText);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                ObservableList<PersonalResponse> data = FXCollections.observableArrayList(getValue().getResults());
                personalTable.setItems(data);
                PagePersonal pagePersonal = new PagePersonal();
                pagePersonal.setTotalPages(1);
                pagePersonal.setNumber(0);
                updatePersonalPages(pagePersonal);
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to search patients: " + getException().getMessage());
            }
        };

        new Thread(searchTask).start();
    }

    public void onFirstPatientPage(ActionEvent actionEvent) {
        if (pagePatientsNumber > 1) {
            pagePatientsNumber = 1;

            loadPatients();
        }
    }

    public void onPreviousPatientPage(ActionEvent actionEvent) {
        if (pagePatientsNumber > 1) {
            pagePatientsNumber--;

            loadPatients();
        }
    }

    public void onNextPatientPage(ActionEvent actionEvent) {
        if (pagePatientsNumber < totalPatientsPages) {
            pagePatientsNumber++;

            loadPatients();
        }
    }

    public void onLastPatientPage(ActionEvent actionEvent) {
        if (pagePatientsNumber < totalPatientsPages) {
            pagePatientsNumber = totalPatientsPages;

            loadPatients();
        }
    }

    public void onFirstPersonalPage(ActionEvent actionEvent) {
        if (pagePersonalNumber > 1) {
            pagePersonalNumber = 1;

            loadPersonal();
        }
    }

    public void onPreviousPersonalPage(ActionEvent actionEvent) {
        if (pagePersonalNumber > 1) {
            pagePersonalNumber--;

            loadPersonal();
        }
    }

    public void onNextPersonalPage(ActionEvent actionEvent) {
        if (pagePersonalNumber < totalPersonalPages) {
            pagePersonalNumber++;

            loadPersonal();
        }
    }

    public void onLastPersonalPage(ActionEvent actionEvent) {
        if (pagePersonalNumber < totalPersonalPages) {
            pagePersonalNumber = totalPersonalPages;

            loadPersonal();
        }
    }

    public void onPatientPageSize(ActionEvent actionEvent) {
        pagePatientsSize = patientPageSizeCombo.getValue();
        pagePatientsNumber = 1;
        loadPatients();
    }

    public void onPersonalPageSize(ActionEvent actionEvent) {
        pagePersonalSize = personalPageSizeCombo.getValue();
        pagePersonalNumber = 1;
        loadPersonal();
    }
}
