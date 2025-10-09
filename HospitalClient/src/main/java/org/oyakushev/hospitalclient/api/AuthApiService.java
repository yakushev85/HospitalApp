package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.HospitalApplication;
import org.oyakushev.hospitalclient.dto.AuthRequest;
import org.oyakushev.hospitalclient.dto.AuthResponse;
import org.oyakushev.hospitalclient.dto.ChangePasswordRequest;
import org.oyakushev.hospitalclient.dto.MessageResponse;

import java.io.IOException;
import java.net.http.HttpRequest;

public class AuthApiService {
    private static final String BASE_URL = "api/";

    private final Gson gson;
    private final ApiClient apiClient;

    public AuthApiService() {
        gson = HospitalApplication.getInstance().getGson();
        apiClient = ApiClient.Instance;
    }

    public MessageResponse getStatus() throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"status").GET().build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), MessageResponse.class);
    }

    public AuthResponse loginUser(AuthRequest authRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"login")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(authRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), AuthResponse.class);
    }

    public MessageResponse logout() throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"logout")
                .GET()
                .build();

        String bodyResponse = apiClient.sendAndGetBody(request).orElseThrow();

        apiClient.clearAuthToken();

        return gson.fromJson(bodyResponse, MessageResponse.class);
    }

    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest) throws IOException, InterruptedException {
        HttpRequest request = apiClient.preBuildRequest(BASE_URL+"password")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(changePasswordRequest)))
                .build();

        return gson.fromJson(apiClient.sendAndGetBody(request).orElseThrow(), MessageResponse.class);
    }
}
