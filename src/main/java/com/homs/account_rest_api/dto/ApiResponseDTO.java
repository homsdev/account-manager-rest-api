package com.homs.account_rest_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
        description = "Standard API response wrapper containing data, metadata, and messages"
)
public class ApiResponseDTO<T> extends RepresentationModel<ApiResponseDTO<T>> {

    @Schema(
            description = "List of error messages",
            example = "[\"Missing request parameters\"]",
            nullable = true
    )
    private List<String> message;

    @Schema(
            description = "The actual payload data when request is successful",
            nullable = true
    )
    private T data;

    @Schema(
            description = "Pagination metadata when response is paginated",
            nullable = true
    )
    private PaginationDTO paginationDTO;

    @Schema(
            description = "Timestamp of the response in UTC",
            example = "2023-11-20T15:30:45.00Z",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Instant timestamp;
}
