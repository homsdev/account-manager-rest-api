package com.homs.account_rest_api.transactions.dto;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class TransactionDto {
    private String id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String alias;
}
