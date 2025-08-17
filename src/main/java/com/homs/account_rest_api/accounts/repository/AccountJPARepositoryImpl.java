package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.utils.TableValidation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * AccountRepository JPA with EntityManager implementation class
 * Profile: This implementation is only active when the dev profile is enabled.
 */
@Slf4j
@Repository
@Profile({"dev"})
@RequiredArgsConstructor
public class AccountJPARepositoryImpl implements AccountRepository {

    private final EntityManager em;

    @PostConstruct
    public void init() {
        log.info("Currently using: {}", this.getClass().getSimpleName());
        TableValidation.validateTable(
                this.getClass().getSimpleName(),
                "cli_account",
                this.em
        );
    }

    @Override
    public List<Account> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> save(Account account) {
        return Optional.empty();
    }

    @Override
    public Integer deleteById(String id) {
        return null;
    }

    @Override
    public Optional<Account> updateBalance(Account account) {
        return Optional.empty();
    }
}
