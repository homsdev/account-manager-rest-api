package com.homs.account_rest_api.transactions.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CreateTransactionRequest {
    private BigDecimal amount;
    private String type;
    private String date;
    private String alias;
    private String categoryId;
}
