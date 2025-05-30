package com.homs.account_rest_api.accounts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class UpdateBalanceDTO {

    @NotNull(message = "Missing balance parameter")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be grater than zero")
    BigDecimal updatedBalance;
}
