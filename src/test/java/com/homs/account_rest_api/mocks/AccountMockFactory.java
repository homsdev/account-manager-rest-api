package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.model.Account;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
public class AccountMockFactory {

    private final Map<String, Account> accounts = new HashMap<>();

    public Account checkingAccount() {
        return accounts.computeIfAbsent("Checking",
                a -> Account.builder()
                        .accountId("acc-checking")
                        .alias("Checking Account")
                        .balance(BigDecimal.valueOf(70_000)).build());
    }

    public Account mainAccount() {
        return accounts.computeIfAbsent("Main",
                a -> Account.builder()
                        .accountId("acc-main").alias("Main Account")
                        .balance(BigDecimal.valueOf(50_000)).build());
    }

    public Account accountWithoutExpenses() {
        return accounts.computeIfAbsent("NoExpenses",
                a -> Account.builder()
                        .accountId("acc-no-expenses")
                        .balance(BigDecimal.valueOf(100_000))
                        .build());
    }
}
