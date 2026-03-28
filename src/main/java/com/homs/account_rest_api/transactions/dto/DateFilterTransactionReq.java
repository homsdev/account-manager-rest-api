package com.homs.account_rest_api.transactions.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DateFilterTransactionReq {
    private Long accountId;
    private Integer month;
    private Integer year;
}
