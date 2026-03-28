package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDTO toDTO(Account account) {

        if (account == null) {
            return null;
        }

        return AccountDTO.builder()
                .id(account.getAccountId())
                .alias(account.getAlias())
                .balance(account.getBalance())
                .type(account.getType())
                .build();
    }

    public CreateAccount toCreateAccount(Account account) {
        if (account == null) {
            return null;
        }

        return CreateAccount.builder()
                .balance(account.getBalance())
                .alias(account.getAlias())
                .type(account.getType())
                .build();
    }

    public Account toEntity(AccountDTO dto) {
        if (dto == null) {
            return null;
        }

        return Account.builder()
                .accountId(dto.getId())
                .alias(dto.getAlias())
                .balance(dto.getBalance())
                .type(dto.getType())
                .build();
    }

    public Account toEntity(CreateAccount dto) {
        if (dto == null) {
            return null;
        }

        return Account.builder()
                .alias(dto.getAlias())
                .balance(dto.getBalance())
                .type(dto.getType())
                .build();
    }
}
