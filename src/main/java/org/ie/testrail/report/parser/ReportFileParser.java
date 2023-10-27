package org.ie.testrail.report.parser;


import org.ie.testrail.model.TestSuite;
import org.ie.testrail.report.processor.ReportFileProcessor;
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
