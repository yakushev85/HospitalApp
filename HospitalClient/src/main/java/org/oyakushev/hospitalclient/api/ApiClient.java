package org.oyakushev.hospitalclient.api;

import com.google.gson.Gson;
import org.oyakushev.hospitalclient.dto.AuthRequest;
import org.oyakushev.hospitalclient.dto.AuthResponse;
import org.oyakushev.hospitalclient.dto.MessageResponse;

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

    private final HttpClient httpClient;
    private final Gson gson;
    private String apiToken;

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

        if (apiToken != null) {
            resBuilder.header("Authorization", "Bearer "+apiToken);
        }

        return resBuilder;
    }

    private Optional<String> sendAndGetBody(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response != null && response.body() != null && response.statusCode() == 200) {
            return Optional.of(response.body());
        } else if (response != null && response.statusCode() == 400) {
            // TODO cast error response to exception
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    public Optional<MessageResponse> getStatus() throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/status").GET().build();

        Optional<String> responseBody = sendAndGetBody(request);

        if (responseBody.isEmpty()) {
            throw new IOException("Server is not accessible.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), MessageResponse.class));
        }
    }

    public Optional<AuthResponse> loginUser(AuthRequest authRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/login")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(authRequest)))
                .build();

        Optional<String> responseBody = sendAndGetBody(request);

        if (responseBody.isEmpty()) {
            throw new IOException("Wrong username or password.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), AuthResponse.class));
        }
    }
}
