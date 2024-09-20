package com.homs.account_rest_api.repository.impl;

import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.rowmappers.AccountRowMapper;
import com.homs.account_rest_api.repository.AccountRepository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository class for managing {@link Account} entities in a MySQL database.
 * <p>
 * This class implements the {@link AccountRepository} interface and provides
 * methods for performing CRUD operations on accounts, including finding, saving,
 * updating, and deleting accounts. It uses a {@link NamedParameterJdbcTemplate}
 * for executing SQL queries with named parameters.
 *
 * <p>SQL queries are injected from the application properties file using the
 * {@link Value} annotation, allowing flexible configuration.</p>
 *
 * @see AccountRepository
 * @see Account
 */
@Repository
@AllArgsConstructor
public class AccountMysqlRepository implements AccountRepository {

    @Value("${account.findAll}")
    private String findAllQuery;
    @Value("${account.save}")
    private String saveQuery;
    @Value("${account.delete}")
    private String deleteQuery;
    @Value("${account.updateBalance}")
    private String updateBalanceQuery;
    @Value("${account.findById}")
    private String findByIdQuery;

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of all accounts from the database.
     * Executes the query defined by {@code findAllQuery} and maps the result
     * to a list of {@link Account} objects using the {@link AccountRowMapper}
     * which is a custom implementation of {@link org.springframework.jdbc.core.RowMapper}
     *
     * @return A {@link List} of {@link Account} objects which may be empty
     * if no accounts are found in the database
     */
    @Override
    public List<Account> findAll() {
        return jdbcTemplate.query(findAllQuery, new AccountRowMapper());
    }

    /**
     * Retrieves an account from the database by its id.
     * Executes a SQL query defined by {@code findById} using the given id
     * The result is mapped to a {@link List} of {@link Account} objects
     * which contains the unique coincidence if found
     *
     * @param id The unique identifier of the account to retrieve
     * @return An {@link Optional} containing the {@link Account} if found,
     * or an empty {@link Optional} if no account was found
     */
    @Override
    public Optional<Account> findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", id);
        return jdbcTemplate.query(findByIdQuery, params, new AccountRowMapper()).stream().findFirst();
    }

    /**
     * Saves a new account into database
     * Executes a SQL query defined by {@code saveQuery} and if operation success
     * returns the number of affected rows. 1 if new account is saved and 0 if account
     * is not saved
     *
     * @param account {@link Account} object containing the data to persist into database
     * @return An {@link Optional} containing the newly persisted {@link Account}
     * or an empty {@link Optional} if account was not persisted
     */
    @Override
    public Optional<Account> save(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", account.getAccountId());
        params.put("alias", account.getAlias());
        params.put("balance", account.getBalance());

        int result = jdbcTemplate.update(saveQuery, params);

        return result > 0 ? Optional.of(account) : Optional.empty();
    }

    /**
     * Deletes an account from database by the given id
     * Executes an SQL query defined by {@code deleteQuery}
     *
     * @param id Unique identifier of the account to delete
     * @return {@link Integer} Number of affected rows. 1 if operation is successful
     * or 0 if operation fails
     */
    @Override
    public Integer deleteById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", id);
        return jdbcTemplate.update(deleteQuery, params);
    }

    /**
     * Updates account balance in database
     *
     * @param account {@link Account} object containing the updated balance
     * @return An @{@link Optional} containing the updated {@link Account} object
     * or an empty {@link Optional} if update operation fails
     */
    @Override
    public Optional<Account> updateBalance(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("updatedBalance", account.getBalance());
        params.put("accountId", account.getAccountId());
        int result = jdbcTemplate.update(updateBalanceQuery, params);

        return result > 0 ? Optional.of(account) : Optional.empty();
    }
}
