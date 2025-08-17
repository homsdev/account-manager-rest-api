package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.utils.TableValidation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * TransactionRepository JPA with EntityManager implementation class
 * Profile: This implementation is only active when the dev profile is enabled.
 */
@Slf4j
@Repository
@Profile({"dev"})
@RequiredArgsConstructor
public class TransactionJPARepositoryImpl implements TransactionRepository {

    private final EntityManager em;

    @PostConstruct
    public void init() {
        log.info("Currently using: {}", this.getClass().getSimpleName());
        TableValidation.validateTable(
                this.getClass().getSimpleName(),
                "cli_transaction",
                this.em
        );
    }

    @Override
    public Optional<Transaction> saveTransaction(Transaction transaction) {
        return Optional.empty();
    }

    @Override
    public List<Transaction> getAllTransactionsByMonth(String accountId, Month month, Year year) {
        return Collections.emptyList();
    }
}
