package io.github.garics2000.testrail.service;

import io.github.garics2000.testrail.TestRailExtension;
import io.github.garics2000.testrail.annotations.AnnotationParser;
import io.github.garics2000.testrail.api.TestRailAPI;
import io.github.garics2000.testrail.report.parser.ReportFileParser;
import io.github.garics2000.testrail.report.parser.xml.XmlParserImpl;
import io.github.garics2000.testrail.report.processor.ReportFileProcessor;
import io.github.garics2000.testrail.report.processor.XmlIReportFileProcessor;
import io.github.garics2000.testrail.tasks.logger.TaskLogger;
import io.github.garics2000.testrail.api.http.HttpClientAdapter;
import io.github.garics2000.testrail.api.request.RequestHandler;
import io.github.garics2000.testrail.api.request.RequestStrategy;
import io.github.garics2000.testrail.api.request.RequestStrategyImpl;
import io.github.garics2000.testrail.report.parser.xml.XmlParser;
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