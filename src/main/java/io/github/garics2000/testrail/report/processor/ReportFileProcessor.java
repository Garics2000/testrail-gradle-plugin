package io.github.garics2000.testrail.report.processor;

import io.github.garics2000.testrail.model.TestSuite;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ReportFileProcessor {
    File[] getReports();

    Map<String, TestSuite> processReports() throws IOException, JDOMException;
}