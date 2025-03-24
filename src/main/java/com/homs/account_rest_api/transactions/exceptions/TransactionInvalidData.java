package com.homs.account_rest_api.transactions.exceptions;

public class TransactionInvalidData extends RuntimeException {

    public TransactionInvalidData(String message) {
        super(message);
    }
}
