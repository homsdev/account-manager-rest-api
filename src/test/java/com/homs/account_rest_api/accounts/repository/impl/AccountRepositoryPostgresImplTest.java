package com.homs.account_rest_api.accounts.repository.impl;

import com.homs.account_rest_api.accounts.mapper.AccountRowMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Profile("dev")
class AccountRepositoryPostgresImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private AccountRepositoryPostgresImpl accountRepositoryPostgresImpl;

    private AccountMockFactory accountMockFactory;

    @BeforeEach
    void setUp() {
        accountMockFactory = new AccountMockFactory();
    }

    @Test
    void findAllShouldReturnListOfFoundAccounts() {
        List<Account> accounts = List.of(
                accountMockFactory.createMainAccount(),
                accountMockFactory.createCheckingAccount());

        when(jdbcTemplate.query(anyString(), any(AccountRowMapper.class)))
                .thenReturn(accounts);

        List<Account> result = accountRepositoryPostgresImpl.findAll();

        assertNotNull(result);
        assertEquals(2, accounts.size());
        assertEquals("Main Account", accounts.get(0).getAlias());
        assertEquals("Checking Account", accounts.get(1).getAlias());
    }


    @Test
    void findByIdShouldReturnOptionalAccount() {
        Account checkingAccount = accountMockFactory.createCheckingAccount();

        when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(AccountRowMapper.class)))
                .thenReturn(Collections.singletonList(checkingAccount));

        accountRepositoryPostgresImpl.findById(1L).ifPresentOrElse(account -> {
            assertEquals(checkingAccount.getAccountId(), account.getAccountId());
            assertEquals(checkingAccount.getAlias(), account.getAlias());
            assertEquals(checkingAccount.getBalance(), account.getBalance());
        }, () -> fail("Expected account to be found"));
    }

    @Test
    void saveShouldReturnPersistedResource() {
        Account mainAccount = accountMockFactory.createMainAccount();

        GeneratedKeyHolder keyHolderMock = mock(GeneratedKeyHolder.class);
        AccountRepositoryPostgresImpl spyRepository = spy(accountRepositoryPostgresImpl);

        when(spyRepository.generateKeyHolder()).thenReturn(keyHolderMock);

        doReturn(1L).when(keyHolderMock).getKey();

        when(jdbcTemplate.update(
                anyString(),
                any(MapSqlParameterSource.class),
                any(GeneratedKeyHolder.class),
                any(String[].class))
        ).thenReturn(1);

        spyRepository.save(mainAccount).ifPresentOrElse(account -> {
            assertEquals(1L, account.getAccountId());
            assertEquals(mainAccount.getAlias(), account.getAlias());
            assertEquals(mainAccount.getBalance(), account.getBalance());
            assertEquals(mainAccount.getType(), account.getType());
        }, () -> fail("Expected account to be persisted"));
    }

    @Test
    void saveShouldHandleZeroRowsAffected() {
        when(jdbcTemplate.update(
                anyString(),
                any(MapSqlParameterSource.class),
                any(GeneratedKeyHolder.class),
                any(String[].class))
        ).thenReturn(0);

        Optional<Account> result = accountRepositoryPostgresImpl.save(accountMockFactory.createMainAccount());

        assertFalse(result.isPresent());
    }

    @Test
    void saveShouldHandleNullKeyHolder() {

        GeneratedKeyHolder mockKeyHolder = mock(GeneratedKeyHolder.class);
        doReturn(null).when(mockKeyHolder).getKey();

        AccountRepositoryPostgresImpl spyRepository = spy(accountRepositoryPostgresImpl);

        doReturn(mockKeyHolder).when(spyRepository).generateKeyHolder();


        when(jdbcTemplate.update(
                anyString(),
                any(MapSqlParameterSource.class),
                any(GeneratedKeyHolder.class),
                any(String[].class))
        ).thenReturn(1);

        Optional<Account> result = spyRepository.save(accountMockFactory.createMainAccount());

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteShouldReturnAffectedRows() {
        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                .thenReturn(1);

        Integer result = accountRepositoryPostgresImpl.deleteById(1L);

        assertEquals(1, result);
    }

    @Test
    void updateBalanceShouldReturnUpdatedAccount() {
        Account account = accountMockFactory.createMainAccount();
        account.setBalance(BigDecimal.TEN);

        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                .thenReturn(1);

        Optional<Account> result = accountRepositoryPostgresImpl.updateBalance(account);

        assertTrue(result.isPresent());
        assertEquals(account.getAccountId(), result.get().getAccountId());
        assertEquals(account.getBalance(), result.get().getBalance());
    }

    @Test
    void updateBalanceShouldReturnEmptyOptionalWhenNoRowsUpdated() {
        when(jdbcTemplate.update(anyString(), any(MapSqlParameterSource.class)))
                .thenReturn(0);

        Optional<Account> result = accountRepositoryPostgresImpl.updateBalance(accountMockFactory.createMainAccount());

        assertTrue(result.isEmpty());
    }
}