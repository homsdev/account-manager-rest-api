package com.homs.account_rest_api.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
@Schema(
        name = "UpdateBalanceRequest",
        description = "Request body for updating the balance of an account"
)
public class UpdateBalance {

    @Schema(
            description = "The new balance of the account",
            example = "15000.00"
    )
    BigDecimal balance;
}
