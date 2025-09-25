package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.dto.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public enum ApiClient {
    Instance;

    public static final String BASE_URL = "http://localhost:8080/";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(2);

    private static final String AUTH_HEADER = "Authorization";

    private final HttpClient httpClient;
    private final Gson gson;
    private String authHeaderValue;

    ApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        gson = new Gson();
    }

    private HttpRequest.Builder preBuildRequest(String apiEndpointUrl) {
        HttpRequest.Builder resBuilder =  HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.BASE_URL+apiEndpointUrl))
                .timeout(DEFAULT_TIMEOUT)
                .header("Content-Type", "application/json");

        if (authHeaderValue != null) {
            resBuilder.header("Authorization", authHeaderValue);
        }

        return resBuilder;
    }

    private Optional<String> sendAndGetBody(HttpRequest request) throws IOException, InterruptedException {
        System.out.println("\nRequest\n" + request.method() + "\n" + request.uri().toString()
                + "\nAuth header: " + request.headers().firstValue(AUTH_HEADER));
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.print("\nResponse\n" + response.statusCode()
                + "\nAuth header: " + response.headers().firstValue(AUTH_HEADER) +
                "\n" + response.body());

        if (response.body() != null && response.statusCode() == 200) {
            response.headers().firstValue(AUTH_HEADER).ifPresent((value) -> authHeaderValue = value);

            return Optional.of(response.body());
        } else if (response.statusCode() == 400) {
            // TODO cast error response to exception
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public MessageResponse getStatus() throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/status").GET().build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), MessageResponse.class);
    }

    public AuthResponse loginUser(AuthRequest authRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/login")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(authRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), AuthResponse.class);
    }

    public MessageResponse logout() throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/logout")
                .GET()
                .build();

        String bodyResponse = sendAndGetBody(request).orElseThrow();

        authHeaderValue = null;

        return gson.fromJson(bodyResponse, MessageResponse.class);
    }

    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/login")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(changePasswordRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), MessageResponse.class);
    }

    public PagePatients getPatients(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PagePatients.class);
    }

    public PatientResponse createPatient(PatientRequest patientRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(patientRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }

    public PatientResponse getPatient(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }

    public PatientResponse updatePatient(long id, PatientRequest patientRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(patientRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }

    public PagePersonal getPersonal(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/personal?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PagePersonal.class);
    }

    public PersonalResponse createPersonal(PersonalRequest personalRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/personal")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(personalRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }

    public PersonalResponse getPersonal(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/personal/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }

    public PersonalResponse updatePersonal(long id, PersonalRequest personalRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/personal/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(personalRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }

    public PageBlood getBlood(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/blood?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageBlood.class);
    }

    public PageBlood getBloodByPatientId(int patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/blood/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageBlood.class);
    }

    public BloodResponse createBlood(BloodRequest bloodRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/blood")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(bloodRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), BloodResponse.class);
    }

    public BloodResponse getBlood(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/blood/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), BloodResponse.class);
    }

    public PageExamination getExamination(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/examinations?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageExamination.class);
    }

    public PageExamination getExaminationByPatientId(int patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/examinations/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageExamination.class);
    }

    public ExaminationResponse createExamination(ExaminationRequest examinationRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/examinations")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(examinationRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), ExaminationResponse.class);
    }

    public ExaminationResponse getExamination(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/examinations/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), ExaminationResponse.class);
    }

    public PagePressure getPressure(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/pressure?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PagePressure.class);
    }

    public PagePressure getPressureByPatientId(int patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/pressure/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PagePressure.class);
    }

    public PressureResponse createPressure(PressureRequest pressureRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/pressure")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(pressureRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PressureResponse.class);
    }

    public PressureResponse getPressure(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/pressure/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PressureResponse.class);
    }

    public PageVaccination getVaccination(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/vaccinations?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageVaccination.class);
    }

    public PageVaccination getVaccinationByPatientId(int patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/vaccinations/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageVaccination.class);
    }

    public VaccinationResponse createVaccination(VaccinationRequest vaccinationRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/vaccinations")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(vaccinationRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), VaccinationResponse.class);
    }

    public VaccinationResponse getVaccination(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/vaccinations/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), VaccinationResponse.class);
    }

    public PageMedicalTest getMedicalTest(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/medical_tests?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageMedicalTest.class);
    }

    public PageMedicalTest getMedicalTestByPatientId(int patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/medical_tests/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), PageMedicalTest.class);
    }

    public MedicalTestResponse createMedicalTest(MedicalTestRequest medicalTestRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/medical_tests")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(medicalTestRequest)))
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), MedicalTestResponse.class);
    }

    public MedicalTestResponse getMedicalTest(long id) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/medical_tests/" + id)
                .GET()
                .build();

        return gson.fromJson(sendAndGetBody(request).orElseThrow(), MedicalTestResponse.class);
    }
}
