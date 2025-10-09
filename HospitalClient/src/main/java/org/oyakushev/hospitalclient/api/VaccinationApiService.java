package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.PageVaccination;
import org.oyakushev.hospitalclient.dto.VaccinationRequest;
import org.oyakushev.hospitalclient.dto.VaccinationResponse;

import java.io.IOException;
import java.net.http.HttpRequest;

public class VaccinationApiService {
    private static final String BASE_URL = "api/vaccinations";

    private final Gson gson;
    private final ApiClient apiClient;

    public VaccinationApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PageVaccination getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageVaccination.class);
    }

    public PageVaccination getByPatientId(long patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageVaccination.class);
    }

    public VaccinationResponse create(VaccinationRequest vaccinationRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(vaccinationRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), VaccinationResponse.class);
    }

    public VaccinationResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), VaccinationResponse.class);
    }

}
