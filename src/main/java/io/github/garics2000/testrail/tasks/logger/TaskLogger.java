package io.github.garics2000.testrail.tasks.logger;

public interface TaskLogger {
    void info(String message);

    void debug(String message);

    void warn(String message);

    void lifecycle(String message);
}