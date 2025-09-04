package com.homs.account_rest_api.exception;

import com.homs.account_rest_api.dto.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<Object>> handleInvalidParametersException(InvalidParametersException ex) {
        log.warn("Executing handleInvalidParametersException: {}", ex.getErrorMessages());
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.builder()
                .message(ex.getErrorMessages())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Executing handleResourceNotFoundException: {}", ex.getMessage());
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.builder()
                .message(Collections.singletonList(ex.getMessage()))
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("Executing handleMethodArgumentNotValidException: {}", ex.getMessage());
        List<String> errorsList = ex.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.builder()
                .message(errorsList)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex
    ) {
        log.warn("Executing handleHttpMessageNotReadableException");
        log.info(ex.getLocalizedMessage());
        String errorMsg = ex.getLocalizedMessage().toLowerCase();
        String responseData = "Error while processing input body";
        if (errorMsg.contains("date")) {
            responseData = "Wrong date format, accepted format is yyyy-mm-dd";
        }

        if (errorMsg.contains("enum")) {
            responseData = "Wrong type, accepted values are [INCOME,EXPENSE]";
        }

        ApiResponseDTO<Object> res = ApiResponseDTO.builder()
                .message(Collections.singletonList(responseData))
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.badRequest().body(res);
    }
}
