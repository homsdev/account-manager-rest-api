package com.homs.account_rest_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
        name = "ErrorResponse",
        description = "Error response returned when an exception occurs"
)
@Builder
@Getter
public class ErrorApiResponse {

    @Schema(
            description = "Error message",
            example = "[\"Error message\"]"
    )
    private List<String> message;

    @Schema(
            description = "Error path",
            example = "/api/v1/accounts"
    )
    private String path;

    @Schema(
            description = "Timestamp when the error occurred",
            example = "2026-03-25T05:00:11"
    )
    private LocalDateTime timestamp;
}
