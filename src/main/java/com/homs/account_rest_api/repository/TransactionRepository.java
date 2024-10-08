package com.homs.account_rest_api.repository;

import com.homs.account_rest_api.model.Transaction;

import java.util.Optional;

/**
 * The TransactionRepository interface defines the contract for
 * handling transaction data persistence.
 * <p>
 * It provides methods to save transactions, encapsulating the
 * underlying data access mechanism and allowing for easier
 * interaction with transaction objects.
 */
public interface TransactionRepository {

    /**
     * Saves the specified transaction
     *
     * @param transaction {@link Transaction} object to persist
     * @return an {@link Optional} containing the saved transaction
     * or an empty {@link Optional} if save operation fails
     */
    public Optional<Transaction> saveTransaction(Transaction transaction);
}
