package org.ie.testrail.api.request;

import org.ie.testrail.api.http.HttpMethod;
import org.ie.testrail.api.response.ApiResponse;
import org.json.JSONObject;

import java.io.IOException;

public interface RequestStrategy {
    ApiResponse execute(String path, HttpMethod method, JSONObject payload) throws IOException, InterruptedException;
}
