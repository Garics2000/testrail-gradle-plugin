package io.github.garics2000.testrail.service;

import io.github.garics2000.testrail.TestRailExtension;
import io.github.garics2000.testrail.api.TestRailAPI;
import io.github.garics2000.testrail.model.TestCase;
import io.github.garics2000.testrail.model.TestSuite;
import io.github.garics2000.testrail.report.parser.ReportFileParser;
import io.github.garics2000.testrail.tasks.logger.TaskLogger;
import io.github.garics2000.testrail.annotations.AnnotationParser;
import io.github.garics2000.testrail.annotations.TestRailCaseID;
import io.github.garics2000.testrail.api.request.RequestHandler;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TestRunOperationService extends OperationService {
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd_HH-mm-ss";
    private static final int INVALID_RUN_ID = 0;

    public TestRunOperationService(TestRailAPI testRailAPI, RequestHandler requestHandler, AnnotationParser annotationParser, ReportFileParser junitXmlReportFileParser, TaskLogger logger, TestRailExtension config) {
        super(annotationParser, junitXmlReportFileParser, testRailAPI, requestHandler, logger, config);

    }

    @Override
    public int export() throws IOException, JDOMException {
        int projectId = getProjectId();
        AtomicInteger exported = new AtomicInteger();
        String testRunBaseName = config.getTestRunName();
        Map<Integer, List<TestCase>> testCasesMap = linkSuiteToCases();

        if (!testCasesMap.isEmpty()) {
            exportTestSuiteResults(projectId, exported, testRunBaseName, testCasesMap);
        }

        return exported.get();
    }

    private void exportTestSuiteResults(int projectId, AtomicInteger exported, String testRunBaseName, Map<Integer, List<TestCase>> testCasesMap) {
        testCasesMap.forEach((suiteId, tests) -> {
            List<Integer> caseIds = tests.stream().map(TestCase::getId).collect(Collectors.toList());
            if (exportTestRun(projectId, suiteId, caseIds, tests, testRunBaseName)) {
                exported.getAndIncrement();
            }
        });
    }

    private List<TestCase> updateWithAnnotatedIds(List<TestCase> testCases) {
        return testCases.stream()
                .peek(tc -> tc.setId(getCaseId(tc)))
                .collect(Collectors.toList());
    }

    private boolean exportTestRun(int projectId, int suiteId, List<Integer> caseIds, List<TestCase> testCases, String runBaseName) {
        if (caseIds.isEmpty()) return false;
        String testRunName = generateTestRunName(runBaseName);
        int createdRunId = testRailAPI.addTestRun(testRunName, projectId, suiteId, caseIds);

        return createdRunId > INVALID_RUN_ID && testRailAPI.addTestsResults(createdRunId, testCases);
    }

    private Map<Integer, List<TestCase>> linkSuiteToCases() throws JDOMException, IOException {
        Map<String, TestSuite> testSuites = junitXmlReportFileParser.parse();
        List<TestSuite> testSuiteList = new ArrayList<>(testSuites.values());
        Map<Integer, List<TestCase>> testCasesMap = new HashMap<>();

        for (TestSuite testSuite : testSuiteList) {
            processTestSuite(testSuite, testCasesMap);
        }
        return testCasesMap;
    }

    private void processTestSuite(TestSuite testSuite, Map<Integer, List<TestCase>> testCasesMap) {
        if (!testSuite.getTestCases().isEmpty()) {
            int suiteId = getSuiteId(testSuite.getTestCases().get(0));
            List<TestCase> annotatedTests = findAnnotatedTests(testSuite.getTestCases(), TestRailCaseID.class);
            annotatedTests = updateWithAnnotatedIds(annotatedTests);
            testCasesMap.computeIfAbsent(suiteId, k -> new ArrayList<>()).addAll(annotatedTests);
        }
    }

    private String generateTestRunName(String basePattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return basePattern + " " + dateFormat.format(new Date());
    }
}