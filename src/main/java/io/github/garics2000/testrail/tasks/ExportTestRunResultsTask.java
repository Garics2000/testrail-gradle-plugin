package io.github.garics2000.testrail.tasks;

import org.gradle.api.tasks.TaskAction;
import org.jdom2.JDOMException;

import java.io.IOException;

public class ExportTestRunResultsTask extends BaseTask {
    @Override
    @TaskAction
    public void execute() {
        int exportedItems = 0;
        logger.lifecycle("Export test run results task started.");
        try {
            exportedItems = taskManager.exportTestRun();
        } catch (IOException | InterruptedException | JDOMException e) {
            logger.warn(e.getMessage());
        }
        logger.lifecycle("Exported test run results of test suites: " + exportedItems);
        logger.lifecycle("Export test run results task finished.");
    }
}
