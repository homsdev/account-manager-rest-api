package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.transactions.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionRowMapperTest {

    private TransactionRowMapper transactionMapper;
    private ResultSet rs;

    @Before
    public void setUp() {
        rs = mock(ResultSet.class);
        transactionMapper = new TransactionRowMapper();
    }

    @Test
    public void shouldMapResultSetToEntity() throws SQLException {
        when(rs.getString("transaction_id"))
                .thenReturn("transactionId");
        when(rs.getString("transaction_date"))
                .thenReturn("2025-11-05");
        when(rs.getString("transaction_alias"))
                .thenReturn("alias");
        when(rs.getString("transaction_type"))
                .thenReturn("EXPENSE");
        when(rs.getBigDecimal("transaction_amount"))
                .thenReturn(BigDecimal.valueOf(1_500));
        when(rs.getString("transaction_account"))
                .thenReturn("accountId");
        when(rs.getString("category_id"))
                .thenReturn("categoryID");

        Transaction transaction = transactionMapper.mapRow(rs, 42);
        assertNotNull(transaction);
    }
}