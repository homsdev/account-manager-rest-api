package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class DummyTransactions {
    private final Transaction groceries;
    private final Transaction services;
    private final Transaction salary;

    public DummyTransactions() {
        DummyCategories dummyCategories = new DummyCategories();
        DummyAccounts dummyAccounts = new DummyAccounts();
        this.groceries = Transaction.builder()
                .transactionId("groceriesId")
                .amount(BigDecimal.valueOf(1_500))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .alias("Groceries")
                .account(dummyAccounts.getMainAccount())
                .category(dummyCategories.getFood())
                .build();
        this.services = Transaction.builder()
                .transactionId("servicesId")
                .amount(BigDecimal.valueOf(678.50))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .alias(" Games Services Payment")
                .account(dummyAccounts.getMainAccount())
                .category(dummyCategories.getGames())
                .build();
        this.salary = Transaction.builder()
                .transactionId("salaryId")
                .amount(BigDecimal.valueOf(10_000))
                .type(TransactionType.INCOME)
                .date(LocalDate.now())
                .alias("Payday")
                .account(dummyAccounts.getMainAccount())
                .category(dummyCategories.getDummy())
                .build();
    }
}
