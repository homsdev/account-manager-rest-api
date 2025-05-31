package com.homs.account_rest_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
public class ApiResponseDTO<T> {
    private List<String> message;
    private T data;
    private PaginationDTO paginationDTO;
    private Instant timestamp;
}
