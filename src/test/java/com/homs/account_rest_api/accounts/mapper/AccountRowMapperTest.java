package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.model.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountRowMapperTest {

    private AccountRowMapper rowMapper;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        rowMapper = new AccountRowMapper();
        resultSet = mock(ResultSet.class);
    }

    @Test
    void mapRowShouldMapAllFieldsCorrectly() throws SQLException {
        LocalDateTime created = LocalDateTime.of(2024, 5, 5, 10, 30);
        LocalDateTime updated = LocalDateTime.of(2024, 5, 6, 11, 45);

        when(resultSet.getLong("account_id")).thenReturn(1L);
        when(resultSet.getString("alias")).thenReturn("Main Account");
        when(resultSet.getBigDecimal("balance")).thenReturn(BigDecimal.valueOf(1500.75));
        when(resultSet.getString("type")).thenReturn("CHECKING");
        when(resultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(created));
        when(resultSet.getTimestamp("updated_date")).thenReturn(Timestamp.valueOf(updated));

        Account result = rowMapper.mapRow(resultSet, 1);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
        assertEquals("Main Account", result.getAlias());
        assertEquals(BigDecimal.valueOf(1500.75), result.getBalance());
        assertEquals(AccountType.CHECKING, result.getType());
        assertEquals(created, result.getCreationDate());
        assertEquals(updated, result.getUpdatedDate());
    }

    @Test
    void mapRowShouldThrowWhenTypeIsInvalid() throws SQLException {
        LocalDateTime created = LocalDateTime.of(2024, 5, 5, 10, 30);
        LocalDateTime updated = LocalDateTime.of(2024, 5, 6, 11, 45);

        when(resultSet.getLong("account_id")).thenReturn(1L);
        when(resultSet.getString("alias")).thenReturn("Main Account");
        when(resultSet.getBigDecimal("balance")).thenReturn(BigDecimal.TEN);
        when(resultSet.getString("type")).thenReturn("INVALID_TYPE");
        when(resultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(created));
        when(resultSet.getTimestamp("updated_date")).thenReturn(Timestamp.valueOf(updated));

        assertThrows(IllegalArgumentException.class, () -> rowMapper.mapRow(resultSet, 1));
    }

    @Test
    void mapRowShouldThrowWhenCreationDateIsNull() throws SQLException {
        LocalDateTime updated = LocalDateTime.of(2024, 5, 6, 11, 45);

        when(resultSet.getTimestamp("creation_date")).thenReturn(null);
        when(resultSet.getTimestamp("updated_date")).thenReturn(Timestamp.valueOf(updated));

        assertThrows(NullPointerException.class, () -> rowMapper.mapRow(resultSet, 1));
    }
}