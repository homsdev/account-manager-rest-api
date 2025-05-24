package com.homs.account_rest_api.transactions.exceptions;

import com.homs.account_rest_api.dto.ApiResponse;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.transactions.controller.TransactionController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice(assignableTypes = TransactionController.class)
public class TransactionExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.toString())
                .data(List.of(ex.getMessage()))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNoValidException(
            MethodArgumentNotValidException ex
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiResponse<String> res = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.toString())
                .data(errors)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        String errorMsg = ex.getLocalizedMessage().toLowerCase();
        String responseData = "Error while processing input body";
        if (errorMsg.contains("date")) {
            responseData = "Wrong date format, accepted format is yyyy-mm-dd";
        }

        if (errorMsg.contains("enum")) {
            responseData = "Wrong type, accepted values are [INCOME,EXPENSE]";
        }

        ApiResponse<String> res = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.toString())
                .data(Collections.singletonList(responseData))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(res);
    }
}
