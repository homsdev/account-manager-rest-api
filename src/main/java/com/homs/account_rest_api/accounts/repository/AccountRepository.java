package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    Optional<Account> save(Account account);

    Integer deleteById(Long id);

    Optional<Account> updateBalance(Account account);

}
