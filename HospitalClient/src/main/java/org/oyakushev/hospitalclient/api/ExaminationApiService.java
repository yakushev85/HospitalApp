package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.ExaminationRequest;
import org.oyakushev.hospitalclient.dto.ExaminationResponse;
import org.oyakushev.hospitalclient.dto.PageExamination;

import java.io.IOException;
import java.net.http.HttpRequest;

public class ExaminationApiService {
    private static final String BASE_URL = "api/examinations";

    private final Gson gson;
    private final ApiClient apiClient;

    public ExaminationApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PageExamination getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageExamination.class);
    }

    public PageExamination getByPatientId(Long patientId, int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/patients/" + patientId + "?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PageExamination.class);
    }

    public ExaminationResponse create(ExaminationRequest examinationRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(examinationRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), ExaminationResponse.class);
    }

    public ExaminationResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), ExaminationResponse.class);
    }
}
