package com.homs.account_rest_api.service;

import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Account service class to handle basic CRUD operations
 * for domain {@link Account}
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Retrieves all accounts in database or an empty {@link List}
     *
     * @return A {@link List} of {@link Account} containing all found accounts
     */
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * Finds an account into database by the provided id
     *
     * @param id Unique identifier of {@link Account}
     * @return The {@link Account} associated with the given id
     * @throws RuntimeException if no account is found
     */
    public Account findById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Requested account was not found"));
    }

    /**
     * Saves new account data into database
     *
     * @param newAccount {@link Account} object containing the info of the new account
     * @return The {@link Account} persisted in DB
     * @throws RuntimeException if account was not created
     */
    public Account saveAccount(Account newAccount) {
        newAccount.setAccountId(UUID.randomUUID().toString());
        return accountRepository.save(newAccount)
                .orElseThrow(()-> new ResourceNotCreatedException("Failed to create new account"));
    }

    /**
     * Deletes account from database by the provided id
     *
     * @param id Unique identifier of the account to delete
     * @return {@link Integer} > 0 if account was created
     * @throws RuntimeException if account was not created
     */
    public Integer deleteById(String id) {
        Integer affectedRows = accountRepository.deleteById(id);
        if (affectedRows < 1) {
            throw new ResourceNotFoundException("Requested account was not found");
        } else {
            return affectedRows;
        }
    }

    /**
     * Updates balance information for the given account
     *
     * @param account {@link Account} containing the updated balance information
     * @return The updated {@link Account}
     * @throws RuntimeException if update operation is not concluded
     */
    public Account updateBalance(Account account) {
        return accountRepository.updateBalance(account)
                .orElseThrow(() -> new ResourceNotFoundException("Requested account was not found"));
    }

}
