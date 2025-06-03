package com.homs.account_rest_api.accounts.dto;

import jakarta.validation.constraints.DecimalMin;
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
public class UpdateBalanceDTO {

    @NotNull(message = "Missing balance parameter")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    BigDecimal updatedBalance;
}
