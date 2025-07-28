package com.homs.account_rest_api.summary.dto;

import com.homs.account_rest_api.transactions.dto.TransactionDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class SummaryDto {
    private BigDecimal totalBalance;
    private BigDecimal creditCardExpenses;
    private BigDecimal savingsGoal;
    private TransactionDto largestExpense;
    private MetadataDto metadata;
}
