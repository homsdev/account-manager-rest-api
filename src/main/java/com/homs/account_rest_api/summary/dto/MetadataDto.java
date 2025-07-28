package com.homs.account_rest_api.summary.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class MetadataDto {
    private BigDecimal totalExpenses;
    private Integer count;
}
