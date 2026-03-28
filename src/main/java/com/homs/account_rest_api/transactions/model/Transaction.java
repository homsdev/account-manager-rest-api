package com.homs.account_rest_api.transactions.model;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class Transaction {
    private Long transactionId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String alias;
    private Account account;
    private Category category;
}
