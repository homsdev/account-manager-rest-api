package com.homs.account_rest_api.exception;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class InvalidParametersException extends RuntimeException {

    private final List<String> errorMessages;

    public InvalidParametersException(String message, Throwable cause, List<String> errorMessages) {
        super(message, cause);
        this.errorMessages = errorMessages;
    }

    public InvalidParametersException(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public InvalidParametersException(String... messages) {
        super("Detected multiple violations");
        this.errorMessages = Arrays.stream(messages).toList();
    }
}
