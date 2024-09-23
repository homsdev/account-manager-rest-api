package com.homs.account_rest_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {
    private BigDecimal accountBalance;
    private String accountAlias;
}
