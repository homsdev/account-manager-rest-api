package com.homs.account_rest_api.exception;

public class InvalidParametersException extends RuntimeException{
    public InvalidParametersException(String message) {
        super(message);
    }

    public InvalidParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
