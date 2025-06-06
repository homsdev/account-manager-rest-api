package com.homs.account_rest_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
public class ApiResponseDTO<T> extends RepresentationModel<ApiResponseDTO<T>>{
    private List<String> message;
    private T data;
    private PaginationDTO paginationDTO;
    private Instant timestamp;
}
