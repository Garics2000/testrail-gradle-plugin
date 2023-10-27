package org.ie.testrail.api.request;

import org.ie.testrail.api.http.HttpClientAdapter;
import org.ie.testrail.api.http.HttpMethod;
import org.ie.testrail.api.response.ApiResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;

public class RequestStrategyImpl implements RequestStrategy {
    private final HttpClientAdapter httpClient;

    public RequestStrategyImpl(HttpClientAdapter httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ApiResponse execute(String path, HttpMethod method, JSONObject payload) throws IOException, InterruptedException {
        String payloadStr = payload.isEmpty() ? "" : payload.toString();
        HttpResponse<String> response = httpClient.sendRequest(path, method, payloadStr);
        return new ApiResponse(response.statusCode(), response.body());
    }
}
