package com.homs.account_rest_api.exception;

import com.homs.account_rest_api.model.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.<Object>builder()
                .data(Collections.emptyList())
                .code(HttpStatus.NOT_FOUND.value())
                .message("Resource not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = ResourceNotCreatedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleResourceNotCreated(ResourceNotCreatedException ex) {
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.<Object>builder()
                .data(Collections.emptyList())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Resource was not created")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponseDTO<Object> errorResponse = ApiResponseDTO.<Object>builder()
                .data(Collections.emptyList())
                .code(HttpStatus.BAD_REQUEST.value())
                .message("ERROR: There are missing parameters")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
