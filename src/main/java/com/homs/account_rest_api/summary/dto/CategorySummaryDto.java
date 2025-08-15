package com.homs.account_rest_api.summary.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class CategorySummaryDto {
    private String category;
    private BigDecimal total;
}
