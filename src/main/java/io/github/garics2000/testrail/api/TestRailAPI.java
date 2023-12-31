package io.github.garics2000.testrail.api;

import io.github.garics2000.testrail.api.http.HttpMethod;
import io.github.garics2000.testrail.api.request.Paths;
import io.github.garics2000.testrail.api.exception.ApiResponseException;
import io.github.garics2000.testrail.api.exception.ApiRuntimeException;
import io.github.garics2000.testrail.api.request.RequestHandler;
import io.github.garics2000.testrail.api.response.ApiResponse;
import io.github.garics2000.testrail.model.TestCase;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static io.github.garics2000.testrail.api.request.helpers.RequestPayloadHelper.*;

public class TestRailAPI {
    private final RequestHandler requestHandler;

    public TestRailAPI(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    public int addTestRun(String runName, int projectId, int suiteId, List<Integer> testRailCaseIds) {
        JSONObject payload = getTestRunPayload(runName, suiteId, testRailCaseIds);
        ApiResponse response = makeRequest(Paths.ADD_TEST_RUN + projectId, HttpMethod.POST, payload, "Error adding test run.");
        return response.getIntValue("id");
    }

    public int addTestCase(String testName, int sectionId, String automationId) {
        JSONObject payload = getTestCasePayload(testName, automationId);
        ApiResponse response = makeRequest(Paths.ADD_CASE + sectionId, HttpMethod.POST, payload, "Error adding test case.");
        return response.getIntValue("id");
    }

    public boolean addTestsResults(int runId, List<TestCase> testResults) {
        JSONObject payload = getTestRunResultsPayload(testResults);
        makeRequest(Paths.ADD_RESULTS + runId, HttpMethod.POST, payload, "Error adding test results.");
        return true;
    }

    private ApiResponse makeRequest(String path, HttpMethod method, JSONObject payload, String errorMsg) {
        try {
            ApiResponse response = requestHandler.makeRequest(path, method, payload);
            if (!response.isSuccess()) {
                throw new ApiResponseException(String.format("Failed to process request. HTTP Status Code: %d, Path: %s", response.getStatusCode(), path));
            }
            return response;
        } catch (IOException | InterruptedException e) {
            throw new ApiRuntimeException(errorMsg, e);
        }
    }
}