package io.github.garics2000.testrail.api.request;

import io.github.garics2000.testrail.api.http.HttpMethod;
import io.github.garics2000.testrail.api.response.ApiResponse;
import org.json.JSONObject;

import java.io.IOException;

public interface RequestStrategy {
    ApiResponse execute(String path, HttpMethod method, JSONObject payload) throws IOException, InterruptedException;
}
