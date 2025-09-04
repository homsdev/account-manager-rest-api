package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.model.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        return AccountDTO.builder()
                .id(account.getAccountId())
                .alias(account.getAlias())
                .balance(account.getBalance())
                .build();
    }

    public static Account toEntity(AccountDTO dto) {
        if (dto == null) {
            return null;
        }

        return Account.builder()
                .accountId(dto.getId())
                .alias(dto.getAlias())
                .balance(dto.getBalance())
                .build();
    }
}
