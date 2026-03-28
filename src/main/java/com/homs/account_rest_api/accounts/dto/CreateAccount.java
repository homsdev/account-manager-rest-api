package com.homs.account_rest_api.accounts.dto;

import com.homs.account_rest_api.accounts.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CreateAccount {

    @Schema(
            description = "The initial balance of the account",
            example = "10000.00"
    )
    private BigDecimal balance;

    @Schema(
            description = "The alias of the account",
            example = "Main Account"
    )
    private String alias;

    @Schema(
            description = "The type of the account",
            example = "SAVINGS"
    )
    private AccountType type;
}
