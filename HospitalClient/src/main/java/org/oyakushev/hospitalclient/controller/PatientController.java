package org.oyakushev.hospitalclient.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.api.ApiClient;
import org.oyakushev.hospitalclient.dto.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PatientController {
    public TextField firstNameField;
    public TextField lastNameField;
    public DatePicker birthdayPicker;
    public TextField addressField;
    public TextField heightField;
    public TextField weightField;
    public Button saveButton;
    public Button cancelButton;
    public Label idLabel;

    public Tab bloodTab;
    public Button newBloodButton;
    public TableView<BloodResponse> bloodTableView;
    public Label bloodPageLabel;

    private int pageBloodNumber = 1;
    private int pageBloodSize = 10;
    private int totalBloodPages = 1;

    public Tab examinationTab;
    public Button newExaminationButton;
    public TableView<ExaminationResponse> examinationTableView;
    public Label examinationPageLabel;

    private int pageExaminationNumber = 1;
    private int pageExaminationSize = 10;
    private int totalExaminationPages = 1;

    public Tab medicalTestTab;
    public Button newMedicalTestButton;
    public TableView<MedicalTestResponse> medicalTestTableView;
    public Label medicalTestPageLabel;

    private int pageMedicalTestNumber = 1;
    private int pageMedicalTestSize = 10;
    private int totalMedicalTestPages = 1;

    public Tab pressureTab;
    public Button newPressureButton;
    public TableView<PressureResponse> pressureTableView;
    public Label pressurePageLabel;

    private int pagePressureNumber = 1;
    private int pagePressureSize = 10;
    private int totalPressurePages = 1;

    public Tab vaccinationTab;
    public Button newVaccinationButton;
    public TableView<VaccinationResponse> vaccinationTableView;
    public Label vaccinationPageLabel;

    private int pageVaccinationNumber = 1;
    private int pageVaccinationSize = 10;
    private int totalVaccinationPages = 1;


    public void initialize() {
        checkRole(HospitalApplication.getInstance().getPersonalRole());

        loadGeneralData();
    }

    private void loadGeneralData() {
        setControlsDisable(true);
        Task<PatientResponse> loadDataTask = new Task<>() {

            @Override
            protected PatientResponse call() throws Exception {
                Long id = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getPatient(id);
            }

            @Override
            protected void succeeded() {
                PatientResponse patientResponse = getValue();
                idLabel.setText("#" + patientResponse.getId());
                firstNameField.setText(patientResponse.getFirstName());
                lastNameField.setText(patientResponse.getLastName());
                LocalDate dobLocalValue = patientResponse.getDob()
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                birthdayPicker.setValue(dobLocalValue);
                addressField.setText(patientResponse.getAddress());
                heightField.setText(String.valueOf(patientResponse.getHeight()));
                weightField.setText(String.valueOf(patientResponse.getWeight()));

                setControlsDisable(false);
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to load patient: " + getException().getMessage());
            }
        };

        new Thread(loadDataTask).start();
    }

    private void checkRole(PersonalRole personalRole) {
        if (personalRole.getIndex() < PersonalRole.Nurse.getIndex()) {
            firstNameField.setDisable(true);
            lastNameField.setDisable(true);
            birthdayPicker.setDisable(true);
            addressField.setDisable(true);
            heightField.setDisable(true);
            weightField.setDisable(true);
            saveButton.setDisable(true);
        } else {
            firstNameField.setDisable(false);
            lastNameField.setDisable(false);
            birthdayPicker.setDisable(false);
            addressField.setDisable(false);
            heightField.setDisable(false);
            weightField.setDisable(false);
            saveButton.setDisable(false);
        }
    }

    private boolean validateData() {
        if (firstNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("First name can't be empty.");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Last name can't be empty.");
            return false;
        }

        if (addressField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Address can't be empty.");
            return false;
        }

        if (heightField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Height can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(heightField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Height must be a number.");
            return false;
        }

        if (weightField.getText().isEmpty()) {
            HospitalApplication.getInstance().showAlertWarning("Weight can't be empty.");
            return false;
        }

        try {
            Double.parseDouble(weightField.getText());
        } catch (NumberFormatException e) {
            HospitalApplication.getInstance().showAlertWarning("Weight must be a number.");
            return false;
        }

        return true;
    }

    private void setControlsDisable(boolean value) {
        saveButton.setDisable(value);
        cancelButton.setDisable(value);
    }

    public void onSaveAction(ActionEvent actionEvent) {
        if (!validateData()) {
            return;
        }

        updateGeneralData();
    }

    private void updateGeneralData() {
        setControlsDisable(true);

        final PatientRequest patientRequest = new PatientRequest();
        patientRequest.setFirstName(firstNameField.getText());
        patientRequest.setLastName(lastNameField.getText());
        patientRequest.setDob(birthdayPicker.getValue().format(DateTimeFormatter.ISO_DATE));
        patientRequest.setAddress(addressField.getText());
        patientRequest.setHeight(Double.parseDouble(heightField.getText()));
        patientRequest.setWeight(Double.parseDouble(weightField.getText()));

        Task<PatientResponse> savePatientTask = new Task<>() {
            @Override
            protected PatientResponse call() throws Exception {
                Long id = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.updatePatient(id, patientRequest);
            }

            @Override
            protected void succeeded() {
                setControlsDisable(false);
                HospitalApplication.getInstance().gotoHomeWindow();
            }

            @Override
            protected void failed() {
                setControlsDisable(false);
                System.err.println("Failed to update patient: " + getException().getMessage());
            }
        };

        new Thread(savePatientTask).start();
    }

    public void onCancelAction(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoHomeWindow();
    }

    public void onBloodTabSelectionChanged(Event event) {
        if (bloodTab.isSelected()) {
            loadBloodData();
        }
    }

    private void loadBloodData() {
        newBloodButton.setDisable(true);

        Task<PageBlood> bloodTask = new Task<>() {
            @Override
            protected PageBlood call() throws Exception {
                Long patientId = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getBloodByPatientId(patientId, pageBloodNumber-1, pageBloodSize);
            }

            @Override
            protected void succeeded() {
                bloodTableView.setItems(FXCollections.observableArrayList(getValue().getContent()));
                newBloodButton.setDisable(false);
                bloodPageLabel.setText(pageBloodNumber + " of " + totalBloodPages + " page");
            }

            @Override
            protected void failed() {
                newBloodButton.setDisable(false);
                System.err.println("Failed to load blood records: " + getException().getMessage());
            }
        };

        new Thread(bloodTask).start();
    }

    public void onNewBlood(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewBloodWindow();
    }

    public void onFirstBloodPage(ActionEvent actionEvent) {
        if (pageBloodNumber > 1) {
            pageBloodNumber = 1;

            loadBloodData();
        }
    }

    public void onPreviousBloodPage(ActionEvent actionEvent) {
        if (pageBloodNumber > 1) {
            pageBloodNumber--;

            loadBloodData();
        }
    }

    public void onNextBloodPage(ActionEvent actionEvent) {
        if (pageBloodNumber < totalBloodPages) {
            pageBloodNumber++;

            loadBloodData();
        }
    }

    public void onLastBloodPage(ActionEvent actionEvent) {
        if (pageBloodNumber < totalBloodPages) {
            pageBloodNumber = totalBloodPages;

            loadBloodData();
        }
    }

    public void onExaminationTabSelectionChanged(Event event) {
        if (examinationTab.isSelected()) {
            loadExaminationData();
        }
    }

    private void loadExaminationData() {
        newExaminationButton.setDisable(true);

        Task<PageExamination> examinationTask = new Task<>() {
            @Override
            protected PageExamination call() throws Exception {
                Long patientId = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getExaminationByPatientId(patientId, pageExaminationNumber-1, pageExaminationSize);
            }

            @Override
            protected void succeeded() {
                examinationTableView.setItems(FXCollections.observableArrayList(getValue().getContent()));
                newExaminationButton.setDisable(false);
                examinationPageLabel.setText(pageExaminationNumber + " of " + totalExaminationPages + " page");
            }

            @Override
            protected void failed() {
                newExaminationButton.setDisable(false);
                System.err.println("Failed to load examination records: " + getException().getMessage());
            }
        };

        new Thread(examinationTask).start();
    }

    public void onNewExamination(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewExaminationWindow();
    }

    public void onFirstExaminationPage(ActionEvent actionEvent) {
        if (pageExaminationNumber > 1) {
            pageExaminationNumber = 1;

            loadExaminationData();
        }
    }

    public void onPreviousExaminationPage(ActionEvent actionEvent) {
        if (pageExaminationNumber > 1) {
            pageExaminationNumber--;

            loadExaminationData();
        }
    }

    public void onNextExaminationPage(ActionEvent actionEvent) {
        if (pageExaminationNumber < totalExaminationPages) {
            pageExaminationNumber++;

            loadExaminationData();
        }
    }

    public void onLastExaminationPage(ActionEvent actionEvent) {
        if (pageExaminationNumber < totalExaminationPages) {
            pageExaminationNumber = totalExaminationPages;

            loadExaminationData();
        }
    }

    public void onMedicalTestTabSelectionChanged(Event event) {
        if (medicalTestTab.isSelected()) {
            loadMedicalTestData();
        }
    }

    private void loadMedicalTestData() {
        newMedicalTestButton.setDisable(true);

        Task<PageMedicalTest> medicalTestTask = new Task<>() {
            @Override
            protected PageMedicalTest call() throws Exception {
                Long patientId = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getMedicalTestByPatientId(patientId, pageMedicalTestNumber-1, pageMedicalTestSize);
            }

            @Override
            protected void succeeded() {
                medicalTestTableView.setItems(FXCollections.observableArrayList(getValue().getContent()));
                newMedicalTestButton.setDisable(false);
                medicalTestPageLabel.setText(pageMedicalTestNumber + " of " + totalMedicalTestPages + " page");
            }

            @Override
            protected void failed() {
                newMedicalTestButton.setDisable(false);
                System.err.println("Failed to load medicalTest records: " + getException().getMessage());
            }
        };

        new Thread(medicalTestTask).start();
    }

    public void onNewMedicalTest(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewMedicalTestWindow();
    }

    public void onFirstMedicalTestPage(ActionEvent actionEvent) {
        if (pageMedicalTestNumber > 1) {
            pageMedicalTestNumber = 1;

            loadMedicalTestData();
        }
    }

    public void onPreviousMedicalTestPage(ActionEvent actionEvent) {
        if (pageMedicalTestNumber > 1) {
            pageMedicalTestNumber--;

            loadMedicalTestData();
        }
    }

    public void onNextMedicalTestPage(ActionEvent actionEvent) {
        if (pageMedicalTestNumber < totalMedicalTestPages) {
            pageMedicalTestNumber++;

            loadMedicalTestData();
        }
    }

    public void onLastMedicalTestPage(ActionEvent actionEvent) {
        if (pageMedicalTestNumber < totalMedicalTestPages) {
            pageMedicalTestNumber = totalMedicalTestPages;

            loadMedicalTestData();
        }
    }

    public void onPressureTabSelectionChanged(Event event) {
        if (pressureTab.isSelected()) {
            loadPressureData();
        }
    }

    private void loadPressureData() {
        newPressureButton.setDisable(true);

        Task<PagePressure> pressureTask = new Task<>() {
            @Override
            protected PagePressure call() throws Exception {
                Long patientId = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getPressureByPatientId(patientId, pagePressureNumber-1, pagePressureSize);
            }

            @Override
            protected void succeeded() {
                pressureTableView.setItems(FXCollections.observableArrayList(getValue().getContent()));
                newPressureButton.setDisable(false);
                pressurePageLabel.setText(pagePressureNumber + " of " + totalPressurePages + " page");
            }

            @Override
            protected void failed() {
                newPressureButton.setDisable(false);
                System.err.println("Failed to load pressure records: " + getException().getMessage());
            }
        };

        new Thread(pressureTask).start();
    }

    public void onNewPressure(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewPressureWindow();
    }

    public void onFirstPressurePage(ActionEvent actionEvent) {
        if (pagePressureNumber > 1) {
            pagePressureNumber = 1;

            loadPressureData();
        }
    }

    public void onPreviousPressurePage(ActionEvent actionEvent) {
        if (pagePressureNumber > 1) {
            pagePressureNumber--;

            loadPressureData();
        }
    }

    public void onNextPressurePage(ActionEvent actionEvent) {
        if (pagePressureNumber < totalPressurePages) {
            pagePressureNumber++;

            loadPressureData();
        }
    }

    public void onLastPressurePage(ActionEvent actionEvent) {
        if (pagePressureNumber < totalPressurePages) {
            pagePressureNumber = totalPressurePages;

            loadPressureData();
        }
    }

    public void onVaccinationTabSelectionChanged(Event event) {
        if (vaccinationTab.isSelected()) {
            loadVaccinationData();
        }
    }

    private void loadVaccinationData() {
        newVaccinationButton.setDisable(true);

        Task<PageVaccination> vaccinationTask = new Task<>() {
            @Override
            protected PageVaccination call() throws Exception {
                Long patientId = HospitalApplication.getInstance().getPatientId();
                return ApiClient.Instance.getVaccinationByPatientId(patientId, pageVaccinationNumber-1, pageVaccinationSize);
            }

            @Override
            protected void succeeded() {
                vaccinationTableView.setItems(FXCollections.observableArrayList(getValue().getContent()));
                newVaccinationButton.setDisable(false);
                vaccinationPageLabel.setText(pageVaccinationNumber + " of " + totalVaccinationPages + " page");
            }

            @Override
            protected void failed() {
                newVaccinationButton.setDisable(false);
                System.err.println("Failed to load vaccination records: " + getException().getMessage());
            }
        };

        new Thread(vaccinationTask).start();
    }

    public void onNewVaccination(ActionEvent actionEvent) {
        HospitalApplication.getInstance().gotoNewVaccinationWindow();
    }

    public void onFirstVaccinationPage(ActionEvent actionEvent) {
        if (pageVaccinationNumber > 1) {
            pageVaccinationNumber = 1;

            loadVaccinationData();
        }
    }

    public void onPreviousVaccinationPage(ActionEvent actionEvent) {
        if (pageVaccinationNumber > 1) {
            pageVaccinationNumber--;

            loadVaccinationData();
        }
    }

    public void onNextVaccinationPage(ActionEvent actionEvent) {
        if (pageVaccinationNumber < totalVaccinationPages) {
            pageVaccinationNumber++;

            loadVaccinationData();
        }
    }

    public void onLastVaccinationPage(ActionEvent actionEvent) {
        if (pageVaccinationNumber < totalVaccinationPages) {
            pageVaccinationNumber = totalVaccinationPages;

            loadVaccinationData();
        }
    }
}
