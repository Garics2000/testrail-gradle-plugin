package org.ie.testrail;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Project;

import java.io.File;

@Getter
@Setter
public class TestRailExtension {

    private String username;
    private String apiKey;
    private String apiUrl;
    private int projectId;

    private int defaultSuiteId = 1;
    private int defaultSectionId = 1;
    private String testRunName = "Test Run";
    private String junitXmlPath = File.separator + "build"
            + File.separator + "test-results"
            + File.separator + "test"
            + File.separator;

    public TestRailExtension(Project project) {

        if (project == null || project.getRootDir() == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }
        this.junitXmlPath = project.getRootDir().getPath() + junitXmlPath;
    }
}
