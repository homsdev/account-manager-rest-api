package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.utils.TableValidation;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

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
        return em.createQuery("SELECT a FROM Account a ORDER BY a.accountId", Account.class)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public List<Account> findAll(Integer pageSize, Integer pageNumber) {

        if (pageSize == null || pageNumber == null || pageSize <= 0 || pageNumber <= 0) {
            throw new InvalidParametersException("Page size and page Number must be positive numbers");
        }

        return em.createQuery("SELECT a FROM Account a ORDER BY a.accountId", Account.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public Optional<Account> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidParametersException("Missing Id property");
        }
        return Optional.ofNullable(em.find(Account.class, id));
    }

    @Override
    @Transactional
    public Optional<Account> save(Account account) {
        if (account == null) {
            throw new InvalidParametersException("Missing account information");
        }
        em.persist(account);
        return Optional.of(account);
    }

    @Override
    @Transactional
    public Integer deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidParametersException("Missing Id property");
        }

        return findById(id).map(account -> {
            em.remove(account);
            return 1;
        }).orElse(0);
    }

    @Override
    public Optional<Account> updateBalance(Account account) {
        if (account == null ) {
            throw new InvalidParametersException("Missing Id property");
        }

        return Optional.ofNullable(
                em.find(Account.class, account.getAccountId(), LockModeType.PESSIMISTIC_WRITE)
        );
    }
}
