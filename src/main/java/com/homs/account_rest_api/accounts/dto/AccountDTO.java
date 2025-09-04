package com.homs.account_rest_api.accounts.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class AccountDTO {
    private String id;
    private String alias;
    private BigDecimal balance;
}
