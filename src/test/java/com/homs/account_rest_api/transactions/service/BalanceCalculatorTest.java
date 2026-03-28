package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceCalculatorTest {

    private final BalanceCalculator balanceCalculator = new BalanceCalculator();

    @Test
    void calculateShouldAddToBalanceWhenIncome() {
        BigDecimal currentBalance = BigDecimal.valueOf(10_500.50);
        BigDecimal amount = BigDecimal.valueOf(1500.75);

        BigDecimal newBalance = balanceCalculator.calculate(currentBalance, amount, TransactionType.INCOME);
        assertEquals(BigDecimal.valueOf(12_001.25), newBalance);
    }

    @Test
    void calculateShouldSubtractFromBalanceWhenExpense() {
        BigDecimal currentBalance = BigDecimal.valueOf(10_500.50);
        BigDecimal amount = BigDecimal.valueOf(500.50);

        BigDecimal newBalance = balanceCalculator.calculate(currentBalance, amount, TransactionType.EXPENSE);
        assertEquals(BigDecimal.valueOf(10_000.00), newBalance);
    }

}