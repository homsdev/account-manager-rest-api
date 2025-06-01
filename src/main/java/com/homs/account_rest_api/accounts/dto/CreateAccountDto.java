package com.homs.account_rest_api.accounts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {

    @NotNull(message = "Missing account balance")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be greater than 0")
    private BigDecimal accountBalance;

    @NotBlank(message = "Missing account alias")
    private String accountAlias;
}
