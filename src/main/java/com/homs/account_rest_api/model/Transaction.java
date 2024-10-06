package com.homs.account_rest_api.model;

import com.homs.account_rest_api.enums.TransactionType;
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
    private Account account;
    private String alias;
}
