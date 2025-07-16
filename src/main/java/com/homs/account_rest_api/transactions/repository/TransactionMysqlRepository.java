package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.transactions.mapper.TransactionRowMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.math.RoundingMode;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static com.homs.account_rest_api.utils.TransactionDataValidation.isValid;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionMysqlRepository implements TransactionRepository {

    @Value("${transaction.save}")
    private String saveTransactionQuery;

    @Value("${transaction.filterByDate}")
    private String filterTransactionsByDate;

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
    public Optional<Transaction> saveTransaction(final Transaction transaction) {

        if (!Boolean.TRUE.equals(isValid(transaction))) throw new TransactionInvalidData("Missing transaction data");
        log.info("Saving transaction into database");
        log.info("amount: {}", transaction.getAmount().setScale(2, RoundingMode.HALF_UP));
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transaction.getTransactionId());
        params.put("amount", transaction.getAmount().setScale(2, RoundingMode.HALF_UP));
        params.put("type", transaction.getType().getValue());
        params.put("date", transaction.getDate().toString());
        params.put("accountId", transaction.getAccount().getAccountId());
        params.put("alias", transaction.getAlias());

        int result = jdbcTemplate.update(saveTransactionQuery, params);
        return result > 0 ? Optional.of(transaction) : Optional.empty();
    }

    /**
     * Retrieves all transactions by the given month and year
     *
     * @param accountId {@link String}
     * @param month     {@link Month}
     * @param year      {@link Year}
     * @return {@link List} of transactions
     */
    @Override
    public List<Transaction> getAllTransactionsByMonth(String accountId, Month month, Year year) {
        if (accountId.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("year", year.getValue());
        params.put("month", month.getValue());
        return jdbcTemplate.query(filterTransactionsByDate, params, new TransactionRowMapper());
    }


}
