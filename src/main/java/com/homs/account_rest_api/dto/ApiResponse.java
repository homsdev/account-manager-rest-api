package com.homs.account_rest_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
@Setter
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private List<T> data;
    private Instant timestamp;
}
