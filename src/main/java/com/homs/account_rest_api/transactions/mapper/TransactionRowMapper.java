package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Transaction.builder()
                .transactionId(rs.getString("transaction_id"))
                .date(LocalDate.parse(rs.getString("transaction_date")))
                .alias(rs.getString("transaction_alias"))
                .type(TransactionType.valueOf(rs.getString("transaction_type")))
                .build();
    }
}
