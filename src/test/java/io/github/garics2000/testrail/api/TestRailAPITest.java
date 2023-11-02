package io.github.garics2000.testrail.api;

import io.github.garics2000.testrail.api.http.HttpMethod;
import io.github.garics2000.testrail.api.exception.ApiResponseException;
import io.github.garics2000.testrail.api.request.RequestHandler;
import io.github.garics2000.testrail.api.response.ApiResponse;
import io.github.garics2000.testrail.model.TestCase;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestRailAPITest {
    private static final int FAKE_ITEM_ID = 1001;
    private static final int GOOD_STATUS = 200;
    private static final int BAD_STATUS = 500;
    private static final String GOOD_RESPONSE_BODY = "{\"id\": " + FAKE_ITEM_ID + "}";

    private final int projectId = 100;
    private final int suiteId = 5;

    @Mock
    private RequestHandler requestHandler;

    @InjectMocks
    private TestRailAPI testRailAPI;

    @Test
    void shouldAddTestRunSuccessfully() throws IOException, InterruptedException {
        List<Integer> caseIds = new ArrayList<>(Arrays.asList(1, 2, 3));
        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(GOOD_STATUS, GOOD_RESPONSE_BODY));

        int testRunId = testRailAPI.addTestRun("Good test run", projectId, suiteId, caseIds);

        assertEquals(testRunId, FAKE_ITEM_ID);
    }

    @Test
    public void shouldFailToAddTestRun() throws IOException, InterruptedException {
        List<Integer> caseIds = new ArrayList<>(Arrays.asList(1, 2, 3));
        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(BAD_STATUS, ""));

        Exception exception = assertThrows(ApiResponseException.class, () -> {
            testRailAPI.addTestRun("Bad test run", projectId, suiteId, caseIds);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(BAD_STATUS)));
    }

    @Test
    public void shouldAddTestCaseSuccessfully() throws IOException, InterruptedException {
        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(GOOD_STATUS, GOOD_RESPONSE_BODY));

        int testCaseId = testRailAPI.addTestCase("New test case", 1, "auto_Id");
        assertEquals(testCaseId, FAKE_ITEM_ID);
    }

    @Test
    public void shouldFailToAddTestCase() throws IOException, InterruptedException {
        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(BAD_STATUS, ""));

        Exception exception = assertThrows(ApiResponseException.class, () -> {
            testRailAPI.addTestCase("Bad test case", 2, "auto_id");
        });

        assertTrue(exception.getMessage().contains(String.valueOf(BAD_STATUS)));
    }

    @Test
    public void shouldAddTestsResultsSuccessfully() throws IOException, InterruptedException {
        int testRunId = 12345;
        List<TestCase> results = Arrays.asList(new TestCase(), new TestCase());

        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(GOOD_STATUS, GOOD_RESPONSE_BODY));

        boolean result = testRailAPI.addTestsResults(testRunId, results);
        assertTrue(result);
    }

    @Test
    public void shouldFailToAddTestsResults() throws IOException, InterruptedException {
        int testRunId = 1000;
        List<TestCase> results = Arrays.asList(new TestCase(), new TestCase());

        when(requestHandler.makeRequest(anyString(), eq(HttpMethod.POST), any(JSONObject.class)))
                .thenReturn(new ApiResponse(BAD_STATUS, ""));

        Exception exception = assertThrows(ApiResponseException.class, () -> {
            testRailAPI.addTestsResults(testRunId, results);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(BAD_STATUS)));
    }
}


