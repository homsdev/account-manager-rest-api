package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findAll();

    Optional<Account> findById(String id);

    Optional<Account> save(Account account);

    Integer deleteById(String id);

    Optional<Account> updateBalance(Account account);

    default List<Account> findAll(Integer pageSize, Integer pageNumber) {
        return Collections.emptyList();
    }
}
