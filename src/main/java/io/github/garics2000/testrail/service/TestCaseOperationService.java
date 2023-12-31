package io.github.garics2000.testrail.service;

import io.github.garics2000.testrail.TestRailExtension;
import io.github.garics2000.testrail.tasks.logger.TaskLogger;
import lombok.SneakyThrows;
import io.github.garics2000.testrail.annotations.AnnotationParser;
import io.github.garics2000.testrail.annotations.AnnotationValue;
import io.github.garics2000.testrail.annotations.TestRailAutomationID;
import io.github.garics2000.testrail.api.TestRailAPI;
import io.github.garics2000.testrail.api.request.RequestHandler;
import io.github.garics2000.testrail.model.TestCase;
import io.github.garics2000.testrail.model.TestSuite;
import io.github.garics2000.testrail.report.parser.ReportFileParser;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TestCaseOperationService extends OperationService {
    private static final String CUSTOM_AUTOMATION_ID = "custom_automation_id";

    public TestCaseOperationService(TestRailAPI testRailAPI, RequestHandler requestHandler, AnnotationParser annotationParser, ReportFileParser junitXmlReportFileParser, TaskLogger logger, TestRailExtension config) {
        super(annotationParser, junitXmlReportFileParser, testRailAPI, requestHandler, logger, config);
    }

    @Override
    public int export() throws IOException, InterruptedException, JDOMException {
        Map<String, TestSuite> testSuites = junitXmlReportFileParser.parse();
        AtomicInteger exported = new AtomicInteger();

        for (TestSuite testSuite : testSuites.values()) {
            exported.addAndGet(exportTestSuiteCases(testSuite));
        }

        return exported.get();
    }

    private int exportTestSuiteCases(TestSuite testSuite) throws IOException, InterruptedException, JDOMException {
        List<TestCase> testCases = testSuite.getTestCases();
        Map<String, List<TestCase>> groupedTestCases = groupTestCasesByClass(testCases);
        AtomicInteger exported = new AtomicInteger();

        for (List<TestCase> testCasesInClass : groupedTestCases.values()) {
            TestCase testCase = testCasesInClass.get(0);
            int suiteId = getSuiteId(testCase);
            int sectionId = getSectionId(testCase);
            exported.addAndGet(exportTestCasesByClass(testCasesInClass, suiteId, sectionId));
        }

        return exported.get();
    }

    private int exportTestCasesByClass(List<TestCase> testCasesInClass, int suiteId, int sectionId) throws IOException, InterruptedException, JDOMException {
        List<String> automationIds = updateAttributeValues(suiteId, CUSTOM_AUTOMATION_ID);
        List<TestCase> annotatedTestCases = findAnnotatedTests(testCasesInClass, TestRailAutomationID.class);
        AtomicInteger exported = new AtomicInteger(0);

        for (TestCase testCase : annotatedTestCases) {
            if (addTestCase(testCase, sectionId, automationIds) > 0) {
                exported.incrementAndGet();
            }
        }

        return exported.get();
    }

    @SneakyThrows
    private int addTestCase(TestCase testCase, int sectionId, List<String> automationIds) {
        Optional<AnnotationValue> optionalValue = annotationParser.getMethodAnnotationValue(
                testCase, TestRailAutomationID.class);

        if (optionalValue.isPresent()) {
            String automationId = optionalValue.get().asString();
            if (automationId != null && !automationIds.contains(automationId)) {
                return testRailAPI.addTestCase(testCase.getTestName(), sectionId, automationId);
            }
        }
        return -1;
    }
}