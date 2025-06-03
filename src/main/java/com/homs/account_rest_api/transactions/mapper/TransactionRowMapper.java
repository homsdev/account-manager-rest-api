package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.transactions.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {

        Account account = Account.builder()
                .accountId(rs.getString("transaction_account"))
                .build();

        return Transaction.builder()
                .transactionId(rs.getString("transaction_id"))
                .date(LocalDate.parse(rs.getString("transaction_date")))
                .alias(rs.getString("transaction_alias"))
                .type(TransactionType.valueOf(rs.getString("transaction_type")))
                .amount(rs.getBigDecimal("transaction_amount"))
                .account(account)
                .build();
    }
}
