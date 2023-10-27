package org.ie.testrail.service;

import org.ie.testrail.TestRailExtension;
import org.ie.testrail.annotations.AnnotationParser;
import org.ie.testrail.api.TestRailAPI;
import org.ie.testrail.api.http.HttpClientAdapter;
import org.ie.testrail.api.request.RequestHandler;
import org.ie.testrail.api.request.RequestStrategy;
import org.ie.testrail.api.request.RequestStrategyImpl;
import org.ie.testrail.report.parser.ReportFileParser;
import org.ie.testrail.report.parser.xml.XmlParser;
import org.ie.testrail.report.parser.xml.XmlParserImpl;
import org.ie.testrail.report.processor.ReportFileProcessor;
import org.ie.testrail.report.processor.XmlIReportFileProcessor;
import org.ie.testrail.tasks.logger.TaskLogger;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.function.Supplier;

public class OperationServiceFactory {
    private final TestRailExtension extension;
    private final TaskLogger logger;

    public OperationServiceFactory(TestRailExtension extension, TaskLogger logger) {
        this.extension = extension;
        this.logger = logger;
    }

    private RequestHandler createRequestHandler() {
        HttpClientAdapter httpClientAdapter = new HttpClientAdapter(java.net.http.HttpClient.newHttpClient(),
                extension.getApiUrl(),
                extension.getUsername(),
                extension.getApiKey());
        RequestStrategy requestStrategy = new RequestStrategyImpl(httpClientAdapter);
        return new RequestHandler(requestStrategy);
    }

    public OperationService getOperationService(ServiceType type) {
        RequestHandler requestHandler = createRequestHandler();
        TestRailAPI testRailAPI = new TestRailAPI(requestHandler);
        AnnotationParser annotationParser = new AnnotationParser();
        Supplier<File> fileFactory = () -> new File(extension.getJunitXmlPath());
        XmlParser xmlParser = new XmlParserImpl(new SAXBuilder());
        ReportFileProcessor fileReader = new XmlIReportFileProcessor(xmlParser, fileFactory);
        ReportFileParser junitXmlReportFileParser = new ReportFileParser(fileReader);

        switch (type) {
            case TEST_CASE:
                return new TestCaseOperationService(testRailAPI, requestHandler, annotationParser, junitXmlReportFileParser, logger, extension);

            case TEST_RUN:
                return new TestRunOperationService(testRailAPI, requestHandler, annotationParser, junitXmlReportFileParser, logger, extension);

            default:
                throw new IllegalArgumentException("Unknown service type: " + type);
        }
    }

    public enum ServiceType {
        TEST_CASE,
        TEST_RUN
    }
}