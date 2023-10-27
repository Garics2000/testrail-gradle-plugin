package org.ie.testrail.api.exception;

public class ApiResponseException extends RuntimeException {
    public ApiResponseException(String message) {
        super(message);
    }
}