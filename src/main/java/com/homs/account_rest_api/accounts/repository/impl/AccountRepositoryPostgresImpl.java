package com.homs.account_rest_api.accounts.repository.impl;

import com.homs.account_rest_api.accounts.mapper.AccountRowMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class AccountRepositoryPostgresImpl implements AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    protected GeneratedKeyHolder generateKeyHolder() {
        return new GeneratedKeyHolder();
    }

    @Override
    public List<Account> findAll() {
        String sql = """
                SELECT * FROM cli_account
                """;
        return jdbcTemplate.query(sql, new AccountRowMapper());
    }

    @Override
    public Optional<Account> findById(Long id) {
        String sql = """
                SELECT * FROM cli_account WHERE account_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(sql, params, new AccountRowMapper()).stream().findFirst();
    }

    @Override
    public Optional<Account> save(Account account) {
        String sql = """
                INSERT INTO cli_account (balance,alias,type) VALUES (:balance,:alias,:type)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("balance", account.getBalance());
        params.addValue("alias", account.getAlias());
        params.addValue("type", account.getType().toString());

        KeyHolder keyHolder = generateKeyHolder();

        int rowsAffected = jdbcTemplate.update(sql, params, keyHolder, new String[]{"account_id"});

        if (rowsAffected <= 0) {
            return Optional.empty();
        }

        if (Objects.isNull(keyHolder.getKey())) {
            return Optional.empty();
        }

        Account newAccount = Account.builder()
                .accountId(keyHolder.getKey().longValue())
                .alias(account.getAlias())
                .type(account.getType())
                .balance(account.getBalance())
                .build();

        return Optional.of(newAccount);
    }

    @Override
    public Integer deleteById(Long id) {
        String sql = """
                DELETE FROM cli_account WHERE account_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.update(sql, params);
    }

    @Override
    public Optional<Account> updateBalance(Account account) {
        String sql = """
                UPDATE cli_account SET balance = :balance WHERE account_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("balance", account.getBalance());
        params.addValue("id", account.getAccountId());

        int rowsAffected = jdbcTemplate.update(sql, params);

        if (rowsAffected <= 0) {
            return Optional.empty();
        }

        return Optional.of(account);
    }

}
