package org.ie.testrail.api.request.helpers;

import org.ie.testrail.model.TestCase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RequestPayloadHelper {

    public static JSONObject getTestCasePayload(String testName, String automationId) {
        JSONObject payload = new JSONObject();
        payload.put("title", testName);
        payload.put("custom_automation_id", automationId);
        return payload;
    }

    public static JSONObject getTestRunPayload(String runName, int suiteId, List<Integer> testRailCaseIds) {
        JSONObject payload = new JSONObject();
        payload.put("name", runName);
        payload.put("suite_id", suiteId);
        payload.put("include_all", false);

        JSONArray caseIdsArray = new JSONArray(testRailCaseIds);
        payload.put("case_ids", caseIdsArray);

        return payload;
    }

    public static JSONObject getTestRunResultsPayload(List<TestCase> testResults) {
        JSONArray payload = new JSONArray();

        for (TestCase result : testResults) {
            JSONObject resultObject = new JSONObject();
            resultObject.put("case_id", result.getId());
            resultObject.put("status_id", result.getTestRailStatus());
            resultObject.put("comment", result.getFailureErrorMessages());
            payload.put(resultObject);
        }

        JSONObject mainObject = new JSONObject();
        mainObject.put("results", payload);

        return mainObject;
    }
}
