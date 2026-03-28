package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.model.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class AccountMockFactory {

    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Creates a main account with default values
     * 1 accountId
     * 50k balance
     * alias "Main Account"
     *
     * @return Account
     */
    public Account createMainAccount() {
        return Account.builder()
                .accountId(1L).alias("Main Account")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(50_000)).build();
    }

    /**
     * Creates a checking account with default values
     * 2 accountId
     * 70k balance
     * alias "Checking Account"
     *
     * @return Account
     */
    public Account createCheckingAccount() {
        return Account.builder()
                .accountId(2L)
                .alias("Checking Account")
                .type(AccountType.CHECKING)
                .balance(BigDecimal.valueOf(70_000)).build();
    }

    /**
     * Returns a list of 2 accounts
     * [1L, "Main Account", 50k]
     * [2L, "Checking Account", 70k]
     *
     * @return List of accounts
     */
    public List<Account> all() {
        return List.of(createCheckingAccount(), createMainAccount());
    }

    public Account accountWithoutExpenses() {
        return Account.builder()
                .accountId(1L)
                .balance(BigDecimal.valueOf(100_000))
                .build();
    }

}
