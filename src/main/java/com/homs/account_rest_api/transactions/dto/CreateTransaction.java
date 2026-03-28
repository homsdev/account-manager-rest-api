package com.homs.account_rest_api.transactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@Schema(description = "DTO for creating a new transaction")
public class CreateTransaction {

    @Schema(description = "Transaction amount",example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Transaction type",example = "INCOME")
    private String type;

    @Schema(description = "Transaction operation date",example = "01-01-2023")
    private String date;

    @Schema(description = "Transaction description",example = "Shopping")
    private String alias;

    @Schema(description = "Transaction category ID",example = "1")
    private Long categoryId;

    @Schema(description = "Transaction account ID",example = "1")
    private Long accountId;
}
