package com.homs.account_rest_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        this("Requested Resource was not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
