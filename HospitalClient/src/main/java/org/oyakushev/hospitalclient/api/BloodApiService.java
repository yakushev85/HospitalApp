package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.BloodRequest;
import org.oyakushev.hospitalclient.dto.BloodResponse;
import org.oyakushev.hospitalclient.dto.PageBlood;

import java.io.IOException;
import java.net.http.HttpRequest;

public class BloodApiService {
    private static final String BASE_URL = "api/blood";

    private final Gson gson;
    private final ApiClient apiClient;

    public BloodApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PageBlood getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageBlood.class);
    }

    public PageBlood getByPatientId(long patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageBlood.class);
    }

    public BloodResponse create(BloodRequest bloodRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(bloodRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), BloodResponse.class);
    }

    public BloodResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), BloodResponse.class);
    }
}
