package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.mapper.AccountRowMapper;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;


import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@Slf4j
@ActiveProfiles("test")
public class AccountMysqlRepositoryTests {

    @Mock
    NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    AccountMysqlRepository accountRepository;

    AccountMockFactory mockFactory;

    @Before
    public void setUp() {
        mockFactory = new AccountMockFactory();
    }

    @Test
    public void findAllShouldReturnAllAccounts() {
        List<Account> expectedAccounts = List.of(
                mockFactory.mainAccount(),
                mockFactory.checkingAccount()
        );
        when(jdbcTemplate.query(isNull(), any(AccountRowMapper.class)))
                .thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountRepository.findAll();

        assertNotNull(actualAccounts);
        assertEquals(expectedAccounts.size(), actualAccounts.size());
    }

    @Test
    public void findByIdShouldReturnAnAccountIfExists() {
        Account mainAccount = mockFactory.mainAccount();
        when(jdbcTemplate.query(isNull(), anyMap(), any(AccountRowMapper.class)))
                .thenReturn(Collections.singletonList(mainAccount));

        Optional<Account> result = accountRepository.findById(mainAccount.getAccountId());

        assertTrue(result.isPresent());
        assertEquals(mainAccount.getAccountId(), result.get().getAccountId());

        verify(jdbcTemplate, atMostOnce()).query(anyString(), anyMap(), any(AccountRowMapper.class));
    }

    @Test
    public void findByIdShouldReturnAnEmptyOptionalWhenIdIsInvalid() {
        when(jdbcTemplate.query(isNull(), anyMap(), any(AccountRowMapper.class)))
                .thenReturn(Collections.emptyList());

        Optional<Account> result = accountRepository.findById("invalidId");

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, times(1))
                .query(isNull(), anyMap(), any(AccountRowMapper.class));
    }

    @Test
    public void saveShouldReturnTheSameAccountWhenSaveIsSuccessful() {
        Account checkingAccount = mockFactory.checkingAccount();
        when(jdbcTemplate.update(isNull(), anyMap())).thenReturn(1);

        Optional<Account> result = accountRepository.save(checkingAccount);

        assertTrue(result.isPresent());
        assertSame(checkingAccount, result.get());
        verify(jdbcTemplate, atMostOnce()).update(isNull(), anyMap());
    }

    @Test
    public void saveShouldReturnEmptyOptionalIfSaveFails() {
        when(jdbcTemplate.update(isNull(), anyMap())).thenReturn(0);

        Optional<Account> result = accountRepository.save(mockFactory.mainAccount());

        assertTrue(result.isEmpty());
        verify(jdbcTemplate, atMostOnce()).update(isNull(), anyMap());
    }

    @Test
    public void deleteByIdShouldReturnMoreThan0RowsAffectedWhenDeleting() {
        when(jdbcTemplate.update(isNull(), anyMap())).thenReturn(1);

        Integer result = accountRepository.deleteById("validId");

        assertEquals(1, result.intValue());
        verify(jdbcTemplate, atMostOnce()).update(isNull(), anyMap());
    }

    @Test
    public void updateBalanceShouldReturnUpdatedAccountWhenUpdatedIsSuccessful() {
        Account account = mockFactory.mainAccount();
        when(jdbcTemplate.update(isNull(), anyMap())).thenReturn(1);

        BigDecimal updatedBalance = BigDecimal.valueOf(50_000.00);
        account.setBalance(updatedBalance);

        Optional<Account> result = accountRepository.updateBalance(account);

        assertTrue(result.isPresent());
        verify(jdbcTemplate, atMostOnce()).update(isNull(), anyMap());
    }

    @Test
    public void updateBalanceShouldReturnEmptyOptionalWhenUpdateFails() {
        when(jdbcTemplate.update(isNull(), anyMap())).thenReturn(0);

        Optional<Account> actualResult = accountRepository.updateBalance(mockFactory.mainAccount());

        assertTrue(actualResult.isEmpty());

        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }
}