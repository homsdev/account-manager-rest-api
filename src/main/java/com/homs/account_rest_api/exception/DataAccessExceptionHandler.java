package com.homs.account_rest_api.exception;

import com.homs.account_rest_api.dto.ErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;

@ControllerAdvice
@Slf4j
public class DataAccessExceptionHandler {

    /**
     * Handles DataAccessException thrown by JPA repositories
     *
     * @param ex - DataAccessException
     * @return ResponseEntity<ErrorApiResponse>
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorApiResponse> handleDataAccessException(DataAccessException ex) {
        log.error("Data access exception occurred", ex);

        ErrorApiResponse body = ErrorApiResponse.builder()
                .message(Collections.singletonList(ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .path("/api/v1/accounts").build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
