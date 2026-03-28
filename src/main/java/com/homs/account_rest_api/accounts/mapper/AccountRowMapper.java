package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.model.AccountType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AccountRowMapper implements RowMapper<Account> {

    @Nullable
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime createdDate = rs.getTimestamp("creation_date").toLocalDateTime();
        LocalDateTime updatedDate = rs.getTimestamp("updated_date").toLocalDateTime();
        return Account.builder()
                .accountId(rs.getLong("account_id"))
                .alias(rs.getString("alias"))
                .balance(rs.getBigDecimal("balance"))
                .type(AccountType.valueOf(rs.getString("type")))
                .creationDate(createdDate)
                .updatedDate(updatedDate)
                .build();
    }
}
