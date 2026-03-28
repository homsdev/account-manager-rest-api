package com.homs.account_rest_api.accounts.dto;

import com.homs.account_rest_api.accounts.model.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@Setter
public class AccountDTO {

    @Schema(
            description = "The unique identifier of the account",
            example = "123"
    )
    private Long id;

    @Schema(
            description = "The alias of the account",
            example = "Main Account"
    )
    private String alias;

    @Schema(
            description = "The current balance of the account",
            example = "50000.00"
    )
    private BigDecimal balance;

    @Schema(
            description = "The type of the account",
            example = "SAVINGS"
    )
    private AccountType type;
}
