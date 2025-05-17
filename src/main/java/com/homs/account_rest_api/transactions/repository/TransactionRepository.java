package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.transactions.model.Transaction;

import java.time.Month;
import java.time.Year;
import java.util.List;
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
    Optional<Transaction> saveTransaction(Transaction transaction);

    /**
     * Retrieves all transactions from DB and only returns those of the given month
     * @param accountId {@link String}
     * @param month {@link Month}
     * @param year {@link Year}
     * @return A {@link List} of {@link Transaction} for the given month
     */
    List<Transaction> getAllTransactionsByMonth(String accountId,Month month, Year year);
}
