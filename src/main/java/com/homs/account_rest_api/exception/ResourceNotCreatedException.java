package com.homs.account_rest_api.exception;

public class ResourceNotCreatedException extends RuntimeException{

    public ResourceNotCreatedException(String message) {
        super(message);
    }

    public ResourceNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
