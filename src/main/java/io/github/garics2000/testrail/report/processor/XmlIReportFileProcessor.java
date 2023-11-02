package io.github.garics2000.testrail.report.processor;

import io.github.garics2000.testrail.model.TestSuite;
import io.github.garics2000.testrail.report.parser.xml.XmlParser;
import io.github.garics2000.testrail.report.parser.xml.XmlValidator;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class XmlIReportFileProcessor implements ReportFileProcessor {
    private static final String TEST_SUITES = "testsuites";
    private static final String TEST_SUITE = "testsuite";

    private final XmlParser xmlParser;
    private final Supplier<File> fileSupplier;
    private final TestCaseProcessor testCaseProcessor;
    private final XmlValidator xmlValidator;

    public XmlIReportFileProcessor(XmlParser xmlParser, Supplier<File> fileSupplier) {
        this.xmlParser = xmlParser;
        this.fileSupplier = fileSupplier;
        this.testCaseProcessor = new TestCaseProcessor();
        this.xmlValidator = new XmlValidator();
    }

    @Override
    public File[] getReports() {
        File folder = fileSupplier.get();
        return folder.listFiles((dir, name) -> name.endsWith(".xml"));
    }

    @Override
    public Map<String, TestSuite> processReports() throws IOException, JDOMException {
        Map<String, TestSuite> testResults = new HashMap<>();
        File[] listOfFiles = getReports();

        if (listOfFiles == null || listOfFiles.length == 0) {
            throw new IOException("No XML report files found in the report directory.");
        }

        for (File file : listOfFiles) {
            processReport(file, testResults);
        }

        return testResults;
    }

    public void processReport(File xmlFile, Map<String, TestSuite> testResults) throws IOException, JDOMException {
        Document document = xmlParser.parse(xmlFile);
        xmlValidator.validateDocument(document, xmlFile);

        Element rootElement = document.getRootElement();
        xmlValidator.validateElement(rootElement, xmlFile);

        List<Element> testSuiteElements = TEST_SUITES.equals(rootElement.getName()) ?
                rootElement.getChildren(TEST_SUITE) :
                Collections.singletonList(rootElement);

        testSuiteElements.forEach(element -> testCaseProcessor.processTestReport(element, testResults));
    }
}
