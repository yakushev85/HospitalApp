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
    private Optional<String> authHeaderValue = Optional.empty();

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

        if (authHeaderValue.isPresent()) {
            resBuilder.header("Authorization", authHeaderValue.orElseThrow());
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
            if (response.headers().firstValue(AUTH_HEADER).isPresent()) {
                authHeaderValue = response.headers().firstValue(AUTH_HEADER);
            }

            return Optional.of(response.body());
        } else if (response.statusCode() == 400) {
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

    public Optional<PagePatients> getPatients(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        Optional<String> responseBody = sendAndGetBody(request);

        if (responseBody.isEmpty()) {
            throw new IOException("Can't fetch patients.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), PagePatients.class));
        }
    }

    public Optional<PatientResponse> createPatient(PatientRequest patientRequest) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/patients")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(patientRequest)))
                .build();

        Optional<String> responseBody = sendAndGetBody(request);

        if (responseBody.isEmpty()) {
            throw new IOException("Doesn't receive body from created patient.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), PatientResponse.class));
        }
    }

    public Optional<PagePersonal> getPersonal(int pageNumber, int pageSize) throws IOException, InterruptedException {
        HttpRequest request = preBuildRequest("api/personal?page=" + pageNumber + "&size=" + pageSize)
                .GET()
                .build();

        Optional<String> responseBody = sendAndGetBody(request);

        if (responseBody.isEmpty()) {
            throw new IOException("Can't fetch personal.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), PagePersonal.class));
        }
    }

    public Optional<PersonalResponse> createPersonal(PersonalRequest personalRequest) throws IOException, InterruptedException {
        HttpRequest httpRequest = preBuildRequest("api/personal")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(personalRequest)))
                .build();

        Optional<String> responseBody = sendAndGetBody(httpRequest);

        if (responseBody.isEmpty()) {
            throw new IOException("Doesn't receive body from created personal.");
        } else {
            return Optional.of(gson.fromJson(responseBody.orElseThrow(), PersonalResponse.class));
        }
    }
}
