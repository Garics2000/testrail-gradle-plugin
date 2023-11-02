package io.github.garics2000.testrail.api.exception;

public class ApiRuntimeException extends RuntimeException {
    public ApiRuntimeException(String message) {
        super(message);
    }

    public ApiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}