package io.github.garics2000.testrail.api.exception;

public class ApiResponseException extends RuntimeException {
    public ApiResponseException(String message) {
        super(message);
    }
}