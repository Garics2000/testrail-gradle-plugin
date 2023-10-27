package org.ie.testrail.report.processor;

import org.ie.testrail.model.TestSuite;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ReportFileProcessor {
    File[] getReports();

    Map<String, TestSuite> processReports() throws IOException, JDOMException;
}