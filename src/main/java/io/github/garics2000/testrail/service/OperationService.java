package io.github.garics2000.testrail.service;

import io.github.garics2000.testrail.TestRailExtension;
import io.github.garics2000.testrail.annotations.*;
import io.github.garics2000.testrail.api.TestRailAPI;
import io.github.garics2000.testrail.api.http.HttpMethod;
import io.github.garics2000.testrail.api.request.Paths;
import io.github.garics2000.testrail.api.request.RequestHandler;
import io.github.garics2000.testrail.api.response.ApiResponse;
import io.github.garics2000.testrail.model.TestCase;
import io.github.garics2000.testrail.report.parser.ReportFileParser;
import io.github.garics2000.testrail.tasks.logger.TaskLogger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jdom2.JDOMException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class OperationService {
    protected final AnnotationParser annotationParser;
    protected final ReportFileParser junitXmlReportFileParser;
    protected final TestRailAPI testRailAPI;
    protected final RequestHandler requestHandler;
    protected final TaskLogger logger;
    protected final TestRailExtension config;


    public abstract int export() throws IOException, InterruptedException, JDOMException;

    public List<String> updateAttributeValues(int suiteId, String attribute) throws IOException, InterruptedException {
        List<String> attributeValues = new ArrayList<>();
        String initialUrl = Paths.GET_CASES + config.getProjectId() + "&suite_id=" + suiteId;

        List<ApiResponse> paginatedResponses = requestHandler.makePaginatedRequest(initialUrl, HttpMethod.GET, new JSONObject());

        for (ApiResponse response : paginatedResponses) {
            if (!response.isSuccess()) {
                logger.warn("Failed to fetch test cases attribute. Status code: " + response.getStatusCode());
                continue;
            }

            setAttributeValues(response.getBody(), attribute, attributeValues);
        }

        return attributeValues;
    }

    private void setAttributeValues(String responseBody, String attribute, List<String> attributeValues) {
        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray testCases = responseObject.getJSONArray("cases");

        for (int i = 0; i < testCases.length(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            String attributeValue = testCase.optString(attribute, null);

            if (attributeValue != null) {
                attributeValues.add(attributeValue);
            }
        }
    }

    public Map<String, List<TestCase>> groupTestCasesByClass(List<TestCase> testCases) {
        return testCases.stream().collect(Collectors.groupingBy(TestCase::getTestClassName));
    }

    public <T extends Annotation> List<TestCase> findAnnotatedTests(List<TestCase> testCases, Class<T> annotationType) {
        return testCases.stream()
                .filter(testCase -> {
                    try {
                        return annotationParser.isAnnotatedTestCase(testCase, annotationType);
                    } catch (ClassNotFoundException e) {
                        logger.warn("Test class from the report is not found: " + e.getMessage());
                    } catch (NoSuchMethodException e) {
                        logger.warn("Method from the report is not found: " + e.getMessage());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public int getSectionId(TestCase testCase) {
        return getClassMetadata(testCase, TestRailSectionID.class, config::getDefaultSectionId);
    }

    @SneakyThrows
    public int getSuiteId(TestCase testCase) {
        return getClassMetadata(testCase, TestRailSuiteID.class, config::getDefaultSuiteId);
    }

    @SneakyThrows
    public int getCaseId(TestCase testCase) {
        return getMethodMetadata(testCase, TestRailCaseID.class, () -> 0);
    }

    private <T extends Annotation> int getClassMetadata(TestCase testCase, Class<T> annotationClass, Supplier<Integer> getDefaultId) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Optional<AnnotationValue> value = annotationParser.getClassAnnotationValue(testCase, annotationClass);
        return value.map(AnnotationValue::asInt).orElse(getDefaultId.get());
    }

    private <T extends Annotation> int getMethodMetadata(TestCase testCase, Class<T> annotationClass, Supplier<Integer> getDefaultId)
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Optional<AnnotationValue> value = annotationParser.getMethodAnnotationValue(testCase, annotationClass);
        return value.map(AnnotationValue::asInt).orElse(getDefaultId.get());
    }

    protected int getProjectId() {
        return config.getProjectId();
    }
}
