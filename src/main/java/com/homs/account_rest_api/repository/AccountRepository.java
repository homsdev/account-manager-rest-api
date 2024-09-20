package com.homs.account_rest_api.repository;

import com.homs.account_rest_api.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    public List<Account> findAll();

    public Optional<Account> findById(String id);

    public Optional<Account> save(Account account);

    public Integer deleteById(String id);

    Optional<Account> updateBalance(Account account);
}
