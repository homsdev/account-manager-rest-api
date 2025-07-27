package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.model.Account;
import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountRowMapperTest {

    private ResultSet rs;
    private AccountRowMapper accountRowMapper;

    @Before
    public void setUp() {
        accountRowMapper = new AccountRowMapper();
        rs = mock(ResultSet.class);
    }

    @Test
    public void shouldMapResultSetToEntity() throws SQLException {
        when(rs.getString("account_id"))
                .thenReturn("accountId");
        when(rs.getString("alias"))
                .thenReturn("accountAlias");
        when(rs.getBigDecimal("balance"))
                .thenReturn(BigDecimal.valueOf(10_000));

        Account account = accountRowMapper.mapRow(rs, 42);
        assertNotNull(account);
        assertEquals("accountId", account.getAccountId());
        assertEquals("accountAlias", account.getAlias());
        assertEquals(BigDecimal.valueOf(10_000), account.getBalance());
    }
}