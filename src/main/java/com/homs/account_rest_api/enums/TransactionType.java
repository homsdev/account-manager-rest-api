package com.homs.account_rest_api.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    EXPENSE("EXPENSE"), INCOME("INCOME");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }
}
