package org.ie.testrail.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.ie.testrail.TestRailExtension;
import org.ie.testrail.service.OperationService;
import org.ie.testrail.service.OperationServiceFactory;
import org.ie.testrail.service.helpers.UrlManager;
import org.ie.testrail.tasks.logger.GradleTaskLoggerAdapter;
import org.ie.testrail.tasks.logger.TaskLogger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;


public abstract class BaseTask extends DefaultTask {

    protected final TestRailExtension extension;
    protected final TaskLogger logger;
    protected TaskManager taskManager;
    private FileCollection classpath;

    private URL[] urls;


    public BaseTask() {
        this.extension = getProject().getExtensions().findByType(TestRailExtension.class);
        this.logger = new GradleTaskLoggerAdapter(getLogger());
        if (this.extension == null) {
            throw new IllegalStateException("TestRail Gradle Plugin must be configured.");
        }
    }

    public abstract void execute();

    @TaskAction
    public void init() {
        initializeFilePaths();
        initializeTaskManager();
    }

    @InputFiles
    public FileCollection getClasspath() {
        return classpath;
    }

    public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }


    private void initializeFilePaths() {
        Set<File> files = getTestRuntimeClasspathFiles();
        URL[] urls = files.stream()
                .map(this::fileToURL)
                .toArray(URL[]::new);

        UrlManager.getInstance().setUrls(urls);
    }


    private Set<File> getTestRuntimeClasspathFiles() {
        return getProject().getExtensions().getByType(JavaPluginExtension.class)
                .getSourceSets().getByName(SourceSet.TEST_SOURCE_SET_NAME)
                .getRuntimeClasspath()
                .getFiles();
    }
    
    private URL fileToURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            logger.warn("Malformed URL exception " + e);
            throw new RuntimeException(e);
        }
    }

    private void initializeTaskManager() {
        OperationServiceFactory factory = new OperationServiceFactory(extension, logger);
        OperationService testCaseService = factory.getOperationService(OperationServiceFactory.ServiceType.TEST_CASE);
        OperationService testRunService = factory.getOperationService(OperationServiceFactory.ServiceType.TEST_RUN);
        this.taskManager = new TaskManager(testCaseService, testRunService);
    }
}