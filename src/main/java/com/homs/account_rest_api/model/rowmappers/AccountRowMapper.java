package com.homs.account_rest_api.model.rowmappers;

import com.homs.account_rest_api.model.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .accountId(rs.getString("alias"))
                .alias(rs.getString("alias"))
                .balance(rs.getBigDecimal("balance"))
                .build();
    }
}
