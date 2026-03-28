package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceCalculator {

    public BigDecimal calculate(BigDecimal currentBalance, BigDecimal amount, TransactionType type) {
        return switch (type) {
            case INCOME -> currentBalance.add(amount);
            case EXPENSE -> currentBalance.subtract(amount);
        };
    }
}
