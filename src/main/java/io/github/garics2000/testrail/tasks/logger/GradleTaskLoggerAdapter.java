package io.github.garics2000.testrail.tasks.logger;

import org.gradle.api.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GradleTaskLoggerAdapter implements TaskLogger {
    private final Logger gradleLogger;
    private final SimpleDateFormat dateFormat;

    public GradleTaskLoggerAdapter(Logger gradleLogger) {
        this.gradleLogger = gradleLogger;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Override
    public void info(String message) {
        gradleLogger.info(addTimestamp(message));
    }

    @Override
    public void debug(String message) {
        gradleLogger.debug(addTimestamp(message));
    }

    @Override
    public void warn(String message) {
        gradleLogger.warn(addTimestamp(message));
    }

    @Override
    public void lifecycle(String message) {
        gradleLogger.lifecycle(addTimestamp(message));
    }

    private String addTimestamp(String message) {
        String timestamp = dateFormat.format(new Date());
        return String.format("[%s] %s", timestamp, message);
    }
}