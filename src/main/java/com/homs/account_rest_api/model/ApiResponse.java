package com.homs.account_rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private List<T> data;
    private String message;
    private Boolean success;
    private Integer code;
}
