package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public @Nullable Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        rs.getLong("transaction_id");
        return Transaction.builder()
                .transactionId(rs.getLong("transaction_id"))
                .amount(rs.getBigDecimal("amount"))
                .type(TransactionType.valueOf(rs.getString("type")))
                .date(rs.getDate("date").toLocalDate())
                .alias(rs.getString("alias"))
                .build();
    }
}
