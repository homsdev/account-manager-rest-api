package com.homs.account_rest_api.transactions.dto;

import com.homs.account_rest_api.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
public class CreateTransactionDTO {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    private String description;
}
