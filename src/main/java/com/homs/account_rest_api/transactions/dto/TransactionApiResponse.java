package com.homs.account_rest_api.transactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@Schema(name = "TransactionApiResponse")
public class TransactionApiResponse{

    @Schema(description = "Transaction data")
    private TransactionDto data;

    @Schema(description = "Timestamp of the response")
    private LocalDateTime timestamp;
}
