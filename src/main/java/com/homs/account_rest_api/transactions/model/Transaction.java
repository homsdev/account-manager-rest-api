package com.homs.account_rest_api.transactions.model;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.accounts.model.Account;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Transaction {
    private String transactionId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String alias;
    private Account account;
    private Category category;
}
