package org.oyakushev.hospitalclient.api;

import org.oyakushev.hospitalclient.HospitalApplication;
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

    private static final String AUTH_HEADER = "Authorization";

    private final HttpClient httpClient;
    private String authHeaderValue;

    ApiClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    HttpRequest.Builder preBuildRequest(String apiEndpointUrl) {
        HttpRequest.Builder resBuilder =  HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.BASE_URL+apiEndpointUrl))
                .timeout(DEFAULT_TIMEOUT)
                .header("Content-Type", "application/json");

        if (authHeaderValue != null) {
            resBuilder.header("Authorization", authHeaderValue);
        }

        return resBuilder;
    }

    Optional<String> sendAndGetBody(HttpRequest request) throws IOException, InterruptedException {
        System.out.println("\nRequest\n" + request.method() + "\n" + request.uri().toString()
                + "\nAuth header: " + request.headers().firstValue(AUTH_HEADER));
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("\nResponse\n" + response.statusCode()
                + "\nAuth header: " + response.headers().firstValue(AUTH_HEADER) +
                "\n" + response.body());

        if (response.body() != null && response.statusCode() == 200) {
            response.headers().firstValue(AUTH_HEADER).ifPresent((value) -> authHeaderValue = value);

            return Optional.of(response.body());
        } else if (response.statusCode() >= 400 && response.statusCode() < 500) {
            MessageResponse errorMessageResponse =
                    HospitalApplication.getInstance().getGson().fromJson(response.body(), MessageResponse.class);

            throw new IOException(errorMessageResponse.getMessage());
        } else {
            return Optional.empty();
        }
    }

    void clearAuthToken() {
        authHeaderValue = null;
    }
}
