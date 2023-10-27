package org.ie.testrail.report.processor;

import org.ie.testrail.model.TestCase;
import org.ie.testrail.model.TestFailure;
import org.ie.testrail.model.TestSuite;
import org.jdom2.Element;

import java.util.List;
import java.util.Map;

public class TestCaseProcessor {
    public void processTestReport(Element testSuiteElement, Map<String, TestSuite> testResults) {
        TestSuite testSuite = processTestSuite(testSuiteElement, testResults);
        testSuiteElement.getChildren("testcase").forEach(testCaseElement -> processTestCase(testCaseElement, testSuite));
    }

    public TestSuite processTestSuite(Element testSuiteElement, Map<String, TestSuite> testResults) {
        String suiteName = testSuiteElement.getAttributeValue("name");
        return testResults.computeIfAbsent(suiteName, k -> {
            TestSuite testSuite = new TestSuite();
            testSuite.setName(suiteName);
            testSuite.setTimestamp(testSuiteElement.getAttributeValue("timestamp"));
            return testSuite;
        });
    }

    public void processTestCase(Element testCaseElement, TestSuite testSuite) {
        TestCase testCase = new TestCase();
        testCase.setTestClassName(testCaseElement.getAttributeValue("classname"));
        testCase.setTestName(testCaseElement.getAttributeValue("name"));
        testCase.setTime(Float.parseFloat(testCaseElement.getAttributeValue("time")));

        addFailures(testCase, testCaseElement.getChildren("failure"));
        addTestErrors(testCase, testCaseElement.getChildren("error"));

        testSuite.getTestCases().add(testCase);
    }

    private void addFailures(TestCase testCase, List<Element> failureElements) {
        failureElements.forEach(failure -> addFailure(failure, testCase));
    }

    private void addTestErrors(TestCase testCase, List<Element> errorElements) {
        errorElements.forEach(error -> addError(error, testCase));
    }

    private void addFailure(Element failureElement, TestCase testCase) {
        addIssue(failureElement, testCase.getFailures());
    }

    private void addError(Element errorElement, TestCase testCase) {
        addIssue(errorElement, testCase.getErrors());
    }

    private void addIssue(Element issueElement, List<TestFailure> issues) {
        TestFailure testIssue = new TestFailure();
        testIssue.setMessage(issueElement.getText());
        issues.add(testIssue);
    }
}
