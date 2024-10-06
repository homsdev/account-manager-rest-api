package com.homs.account_rest_api.repository.impl;

import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionMysqlRepository implements TransactionRepository {

    @Value("${transaction.save}")
    private String saveTransactionQuery;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Saves a new transaction into datasource
     * Executes a SQL query defined by {@code saveTransactionQuery} and if operation success
     * returns the new transaction information
     *
     * @param transaction {@link Transaction} object containing the data to save
     * @return An {@link Optional} that contains the recent saved {@link Transaction}
     * or an empty {@link Optional} if transaction was not saved
     */
    @Override
    public Optional<Transaction> saveTransaction(Transaction transaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transaction.getTransactionId());
        params.put("amount", transaction.getAmount());
        params.put("type", transaction.getType().getValue());
        params.put("date", transaction.getDate().toString());
        params.put("accountId", transaction.getAccount().getAccountId());
        params.put("alias", transaction.getAlias());

        int result = jdbcTemplate.update(saveTransactionQuery, params);
        return result > 0 ? Optional.of(transaction) : Optional.empty();
    }
}
