package com.homs.account_rest_api.service;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.repository.AccountRepository;
import com.homs.account_rest_api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Transaction service class provides methods to perform CRUD
 * operations over transactions
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * Saves the specified transaction
     * <p>
     * This method saves a new {@link Transaction} associated to an existing account into database
     * If {@link TransactionType} is EXPENSE it subtracts transaction amount from the associated account balance
     * else if is an INCOME ads the transaction amount to the associated account balance
     *
     * @param transaction {@link Transaction} object to be saved
     * @param accountId   {@link String} Id from the account associated with the transaction
     * @return Saved {@link Transaction} object
     * @throws ResourceNotFoundException   if the associated account does not exist
     * @throws ResourceNotCreatedException if there is an error while persisting the transaction
     */
    @Transactional
    public Transaction saveTransaction(Transaction transaction, String accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Associated account does not exist"));

        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAccount(account);

        BigDecimal actualBalance = account.getBalance();
        BigDecimal updatedBalance = null;


        if (transaction.getType() == TransactionType.EXPENSE) {
            updatedBalance = actualBalance.subtract(transaction.getAmount());
        }

        if (transaction.getType() == TransactionType.INCOME) {
            updatedBalance = actualBalance.add(transaction.getAmount());
        }

        account.setBalance(updatedBalance);

        accountRepository.updateBalance(account)
                .orElseThrow(() -> new ResourceNotFoundException("Error while updating transaction balance"));

        return transactionRepository.saveTransaction(transaction)
                .orElseThrow(() -> new ResourceNotCreatedException("Error while saving transaction"));
    }

}
