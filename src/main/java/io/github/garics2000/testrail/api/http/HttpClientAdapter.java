package io.github.garics2000.testrail.api.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class HttpClientAdapter {
    private final java.net.http.HttpClient httpClient;
    private final String apiUrl;
    private final String authHeader;

    public HttpClientAdapter(java.net.http.HttpClient httpClient, String apiUrl, String username, String apiKey) {
        this.httpClient = httpClient;
        this.apiUrl = apiUrl;
        this.authHeader = createAuthHeader(username, apiKey);
    }

    public HttpResponse<String> sendRequest(String path, HttpMethod method, String payload) throws IOException, InterruptedException {
        HttpRequest request = buildHttpRequest(path, method, payload);
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest buildHttpRequest(String path, HttpMethod method, String payload) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + path))
                .method(method.name(), HttpRequest.BodyPublishers.ofString(payload))
                .header("Authorization", authHeader);

        if (!payload.isEmpty()) {
            requestBuilder.header("Content-Type", "application/json");
        }

        return requestBuilder.build();
    }

    private String createAuthHeader(String username, String apiKey) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + apiKey).getBytes());
    }
}