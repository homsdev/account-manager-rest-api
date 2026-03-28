package com.homs.account_rest_api.transactions.repository.impl;

import com.homs.account_rest_api.transactions.mapper.TransactionRowMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryPostgresImpl implements TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    protected GeneratedKeyHolder generateKeyHolder() {
        return new GeneratedKeyHolder();
    }

    @Override
    public Optional<Transaction> saveTransaction(Transaction transaction) {
        log.info("Persisting transaction data: {}", transaction);

        String sql = """
                INSERT INTO cli_transaction (amount,type,date,alias,transaction_account,transaction_category) 
                VALUES (:amount,:type,:date,:alias,:accountId,:categoryId)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("amount", transaction.getAmount());
        params.addValue("type", transaction.getType().toString());
        params.addValue("date", transaction.getDate());
        params.addValue("alias", transaction.getAlias());
        params.addValue("accountId", transaction.getAccount().getAccountId());
        params.addValue("categoryId", transaction.getCategory().getId());

        GeneratedKeyHolder keyHolder = generateKeyHolder();

        int update = jdbcTemplate.update(sql, params, keyHolder, new String[]{"transaction_id"});

        if (update <= 0) {
            return Optional.empty();
        }

        if (Objects.isNull(keyHolder.getKey())) {
            return Optional.empty();
        }

        Long id = keyHolder.getKey().longValue();

        Transaction createdTransaction = Transaction.builder()
                .transactionId(id)
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .alias(transaction.getAlias())
                .account(transaction.getAccount())
                .category(transaction.getCategory())
                .build();


        return Optional.of(createdTransaction);
    }

    @Override
    public List<Transaction> getAllTransactionsByMonth(Long accountId, Month month, Year year) {
        String sql = """
                SELECT * FROM cli_transaction
                WHERE transaction_account = :accountId
                AND date >= :startDate
                AND date < :endDate
                """;
        LocalDate startDate = LocalDate.of(year.getValue(), month, 1);
        LocalDate endDate = startDate.plusMonths(1);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("accountId", accountId);
        params.addValue("startDate", startDate);
        params.addValue("endDate", endDate);

        return jdbcTemplate.query(sql, params, new TransactionRowMapper());
    }
}
