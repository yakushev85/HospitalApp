package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.MedicalTestRequest;
import org.oyakushev.hospitalclient.dto.MedicalTestResponse;
import org.oyakushev.hospitalclient.dto.PageMedicalTest;

import java.io.IOException;
import java.net.http.HttpRequest;

public class MedicalTestApiService {
    private static final String BASE_URL = "api/medical_tests";

    private final Gson gson;
    private final ApiClient apiClient;

    public MedicalTestApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PageMedicalTest getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageMedicalTest.class);
    }

    public PageMedicalTest getByPatientId(long patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageMedicalTest.class);
    }

    public MedicalTestResponse create(MedicalTestRequest medicalTestRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(medicalTestRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), MedicalTestResponse.class);
    }

    public MedicalTestResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), MedicalTestResponse.class);
    }
}
