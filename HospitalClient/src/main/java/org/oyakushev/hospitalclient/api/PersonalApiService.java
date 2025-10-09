package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.PagePersonal;
import org.oyakushev.hospitalclient.dto.PersonalRequest;
import org.oyakushev.hospitalclient.dto.PersonalResponse;
import org.oyakushev.hospitalclient.dto.SearchPersonalResultsResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public class PersonalApiService {
    private static final String BASE_URL = "api/personal";

    private final Gson gson;
    private final ApiClient apiClient;

    public PersonalApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public PagePersonal getPage(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PagePersonal.class);
    }

    public SearchPersonalResultsResponse search(String text) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/search?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8))
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), SearchPersonalResultsResponse.class);
    }

    public PersonalResponse create(PersonalRequest personalRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(personalRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }

    public PersonalResponse getById(long id) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .GET()
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }

    public PersonalResponse update(long id, PersonalRequest personalRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(personalRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), PersonalResponse.class);
    }
}
