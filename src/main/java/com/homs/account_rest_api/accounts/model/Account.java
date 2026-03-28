package com.homs.account_rest_api.accounts.model;

import com.homs.account_rest_api.transactions.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class Account {

    private Long accountId;
    private String alias;
    private BigDecimal balance;
    private AccountType type;
    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
}
