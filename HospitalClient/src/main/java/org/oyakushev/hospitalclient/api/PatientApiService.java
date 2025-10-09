package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.PagePatients;
import org.oyakushev.hospitalclient.dto.PatientRequest;
import org.oyakushev.hospitalclient.dto.PatientResponse;
import org.oyakushev.hospitalclient.dto.SearchPatientResultsResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class PatientApiService {
    private static final String BASE_URL = "api/patients";

    private final Gson gson;
    private final ApiClient apiClient;

    public PatientApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PagePatients getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PagePatients.class);
    }

    public SearchPatientResultsResponse search(String text) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/search?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8))
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), SearchPatientResultsResponse.class);
    }

    public PatientResponse create(PatientRequest patientRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(patientRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }

    public PatientResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }

    public PatientResponse update(long id, PatientRequest patientRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(patientRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PatientResponse.class);
    }
}
