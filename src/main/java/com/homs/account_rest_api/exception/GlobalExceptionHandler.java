package com.homs.account_rest_api.exception;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidParametersException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleInvalidParametersException(InvalidParametersException ex){
        log.info("Executing handleInvalidParametersException: {}",ex.getMessage());
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.builder()
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.info("Executing handleResourceNotFoundException: {}",ex.getMessage());
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.builder()
                .message(ex.getMessage())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
