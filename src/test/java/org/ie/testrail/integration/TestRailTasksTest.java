package org.ie.testrail.integration;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.ie.testrail.TestRailExtension;
import org.ie.testrail.tasks.BaseTask;
import org.ie.testrail.tasks.ExportAutoTestsTask;
import org.ie.testrail.tasks.ExportTestRunResultsTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRailTasksTest {
    private static final int PROJECT_ID = 1;
    private static final String REPORT_PATH = System.getenv("REPORT_PATH");
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_URL = System.getenv("API_URL");
    private static final String USER_NAME = System.getenv("USER_NAME");


    private Project project;
    private TestRailExtension config;

    @BeforeEach
    public void setUp() {
        initProject();
        initTestRailExtension();
    }

    @Test
    public void shouldExportTestsSuccessfully() {
        executeTask("testExportTestCase", ExportAutoTestsTask.class);
    }

    @Test
    public void shouldExportTestRunSuccessfully() {
        executeTask("testExportTestRun", ExportTestRunResultsTask.class);
    }

    private void initProject() {
        project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
    }

    private void initTestRailExtension() {
        config = new TestRailExtension(project);
        config.setApiKey(API_KEY);
        config.setProjectId(PROJECT_ID);
        config.setApiUrl(API_URL);
        config.setUsername(USER_NAME);
        config.setJunitXmlPath(REPORT_PATH);
        project.getExtensions().add("testRailExtension", config);
    }

    private <T extends BaseTask> void executeTask(String taskName, Class<T> taskType) {
        T task = project.getTasks().create(taskName, taskType);
        task.init();
        task.execute();
    }
}
