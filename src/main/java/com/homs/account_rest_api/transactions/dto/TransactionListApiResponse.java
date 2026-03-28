package com.homs.account_rest_api.transactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Schema(description = "Transaction List API Response")
public class TransactionListApiResponse {

    @Schema(description = "List of transactions")
    private List<TransactionDto> data;

    @Schema(description = "Total number of transactions", example = "100")
    private Integer total;

    @Schema(description = "Timestamp of the response", example = "2023-09-20T12:34:56")
    private LocalDateTime timestamp;
}
