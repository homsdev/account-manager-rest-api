package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.accounts.model.Account;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DummyAccounts {
    private final Account mainAccount;

    public DummyAccounts() {
        this.mainAccount = Account.builder()
                .accountId("mainAccountId")
                .balance(BigDecimal.valueOf(50_000))
                .alias("Main Account")
                .build();
    }
}
