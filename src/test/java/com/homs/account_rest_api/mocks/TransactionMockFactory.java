package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TransactionMockFactory {

    private final Map<String, List<Transaction>> mainAccountTransactions = new HashMap<>();
    private final Map<String, List<Transaction>> checkingAccountTransactions = new HashMap<>();
    private final CategoryMockFactory categoryMockFactory;
    private final AccountMockFactory accountMockFactory;

    public TransactionMockFactory() {
        categoryMockFactory = new CategoryMockFactory();
        accountMockFactory = new AccountMockFactory();
        categoryMockFactory.all();
    }

    public Transaction createExpense(Account account, Category category, Double amount, String alias) {
        return Transaction.builder()
                .account(account)
                .category(category)
                .transactionId(String.format("tr-%s", alias))
                .amount(BigDecimal.valueOf(amount))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .alias(alias)
                .build();
    }

    public Transaction createIncome(Account account, Category category, Double amount, String alias) {
        return Transaction.builder()
                .account(account)
                .category(category)
                .transactionId(String.format("tr-%s", alias))
                .amount(BigDecimal.valueOf(amount))
                .type(TransactionType.INCOME)
                .date(LocalDate.now())
                .alias(alias)
                .build();
    }

    public List<Transaction> createMainAccountTransactions() {
        Account mainAccount = accountMockFactory.mainAccount();
        Category food = categoryMockFactory.foodCategory();
        Category transportation = categoryMockFactory.transportation();
        Category salary = categoryMockFactory.salary();

        List<Transaction> transactions = List.of(
                createExpense(mainAccount, food, 1_000.0, "Groceries"),
                createExpense(mainAccount, food, 750.0, "Pizza"),
                createExpense(mainAccount, transportation, 1_000.0, "Taxi"),
                createIncome(mainAccount, salary, 11_600.0, "Salary")
        );

        return mainAccountTransactions.computeIfAbsent("Main",
                txs -> transactions);
    }

    public List<Transaction> createCheckingAccountTransactions() {
        Account checkingAccount = accountMockFactory.checkingAccount();
        Category food = categoryMockFactory.foodCategory();
        Category games = categoryMockFactory.gamesCategory();
        List<Transaction> transactions = List.of(
                createExpense(checkingAccount, food, 1_000.0, "Restaurants"),
                createExpense(checkingAccount, games, 2_000.0, "Videogames")
        );

        return checkingAccountTransactions.computeIfAbsent("Checking",
                txs -> transactions);
    }
}
