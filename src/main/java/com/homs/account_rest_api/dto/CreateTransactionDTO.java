package com.homs.account_rest_api.dto;


import com.homs.account_rest_api.annotations.ValidDate;
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
    private String transactionType;

    @NotBlank
    @ValidDate(format = "yyyy-mm-dd")
    private String date;
}
