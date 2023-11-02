package io.github.garics2000.testrail.report.parser;


import io.github.garics2000.testrail.model.TestSuite;
import io.github.garics2000.testrail.report.processor.ReportFileProcessor;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.Map;

public class ReportFileParser {
    private final ReportFileProcessor reportFileProcessor;

    public ReportFileParser(ReportFileProcessor fileReader) {
        this.reportFileProcessor = fileReader;

    }

    public Map<String, TestSuite> parse() throws IOException, JDOMException {
        return reportFileProcessor.processReports();
    }
}
