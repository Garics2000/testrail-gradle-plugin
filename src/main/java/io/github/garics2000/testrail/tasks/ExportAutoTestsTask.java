package io.github.garics2000.testrail.tasks;

import org.gradle.api.tasks.TaskAction;
import org.jdom2.JDOMException;

import java.io.IOException;

public class ExportAutoTestsTask extends BaseTask {
    @Override
    @TaskAction
    public void execute() {
        int exportedItems = 0;
        logger.lifecycle("Export auto tests task started.");
        try {
            exportedItems = taskManager.exportTestCases();

        } catch (IOException | InterruptedException | JDOMException e) {
            logger.warn(e.getMessage());
        }
        logger.lifecycle("Exported auto tests: " + exportedItems);
        logger.lifecycle("Export auto tests task finished.");
    }
}
