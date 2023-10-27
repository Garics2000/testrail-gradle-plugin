package org.ie.testrail.tasks;

import org.ie.testrail.service.OperationService;
import org.jdom2.JDOMException;

import java.io.IOException;

public class TaskManager {
    
    private final OperationService testCaseService;
    private final OperationService testRunService;

    public TaskManager(OperationService testCaseService, OperationService testRunService) {
        this.testCaseService = testCaseService;
        this.testRunService = testRunService;
    }

    public int exportTestRun() throws IOException, InterruptedException, JDOMException {
        return testRunService.export();
    }

    public int exportTestCases() throws IOException, InterruptedException, JDOMException {
        return testCaseService.export();
    }
}