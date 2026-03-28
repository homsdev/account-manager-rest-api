package com.homs.account_rest_api.accounts.service;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.dto.UpdateBalance;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();

    AccountDTO findById(Long id);

    AccountDTO saveAccount(CreateAccount dto);

    Integer deleteById(Long id);

    AccountDTO updateBalance(Long id, UpdateBalance dto);
}
