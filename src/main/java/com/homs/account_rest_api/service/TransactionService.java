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


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

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
