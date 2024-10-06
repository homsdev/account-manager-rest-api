package com.homs.account_rest_api.repository;

import com.homs.account_rest_api.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    public Optional<Transaction> saveTransaction(Transaction transaction);
}
