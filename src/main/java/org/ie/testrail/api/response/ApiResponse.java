package org.ie.testrail.api.response;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import static java.net.HttpURLConnection.HTTP_OK;

@Getter
@Slf4j
public class ApiResponse {
    private final int statusCode;
    private final String body;

    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public boolean isSuccess() {
        return statusCode == HTTP_OK;
    }

    public int getIntValue(String key) {
        try {
            JSONObject jsonResponse = new JSONObject(getBody());
            return jsonResponse.getInt(key);
        } catch (JSONException e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            return 0;
        }
    }
}