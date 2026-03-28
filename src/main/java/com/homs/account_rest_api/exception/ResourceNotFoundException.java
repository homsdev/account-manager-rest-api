package com.homs.account_rest_api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Requested Resource was not found");
    }

    public ResourceNotFoundException(String id) {
        super(String.format("Resource with ID: %s not found", id));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
