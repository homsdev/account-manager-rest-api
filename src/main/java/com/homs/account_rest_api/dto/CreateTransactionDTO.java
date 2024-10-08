package com.homs.account_rest_api.dto;


import com.homs.account_rest_api.annotations.ValidDate;
import com.homs.account_rest_api.annotations.ValidType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionDTO {
    @NotBlank
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    @ValidType(type = "TRANSACTION_TYPE")
    private String transactionType;

    @NotBlank
    @ValidDate(format = "yyyy-MM-dd")
    private String date;
}
