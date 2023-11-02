package io.github.garics2000.testrail;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import io.github.garics2000.testrail.tasks.ExportAutoTestsTask;
import io.github.garics2000.testrail.tasks.ExportTestRunResultsTask;

public class TestRailPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("testRail", TestRailExtension.class, project);
        project.getTasks().create("exportTestRunResults", ExportTestRunResultsTask.class)
                .setClasspath(project.getConfigurations().getByName("runtimeClasspath"));
        project.getTasks().create("exportAutoTests", ExportAutoTestsTask.class)
                .setClasspath(project.getConfigurations().getByName("runtimeClasspath"));
    }
}
