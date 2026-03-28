package com.homs.account_rest_api.transactions.dto;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class TransactionDto {

    @Schema(description = "Transaction ID",example = "1")
    private Long id;

    @Schema(description = "Transaction amount",example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Transaction type",example = "INCOME,EXPENSE")
    private TransactionType type;

    @Schema(description = "Transaction operation date",example = "2023-01-01")
    private LocalDate date;

    @Schema(description = "Transaction description",example = "Shopping")
    private String description;
}
