package com.homs.account_rest_api.exception;

import com.homs.account_rest_api.dto.ErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles custom {@link InvalidParametersException} it occurs when a service is called with incorrect
     * or missing params
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = InvalidParametersException.class)
    public ResponseEntity<ErrorApiResponse> handleInvalidParametersException(InvalidParametersException ex) {
        log.warn("Executing handleInvalidParametersException: {}", ex.getErrorMessages());
        ErrorApiResponse response = ErrorApiResponse.builder().message(ex.getErrorMessages()).path("/api/v1").timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Executing handleResourceNotFoundException: {}", ex.getMessage());
        ErrorApiResponse errorResponse = ErrorApiResponse.builder()
                .message(Collections.singletonList(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Executing handleMethodArgumentNotValidException: {}", ex.getMessage());
        List<String> errorsList = ex.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ErrorApiResponse errorResponse = ErrorApiResponse.builder()
                .message(errorsList)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
