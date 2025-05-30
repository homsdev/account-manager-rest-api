package com.homs.account_rest_api.accounts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {

    @NotNull(message = "Missing account balance")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be greater than 0")
    private BigDecimal accountBalance;

    @NotBlank(message = "Missing account alias")
    private String accountAlias;
}
