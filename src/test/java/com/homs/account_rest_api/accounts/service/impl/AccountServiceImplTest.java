package com.homs.account_rest_api.accounts.service.impl;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.dto.UpdateBalance;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.model.AccountType;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountMockFactory accountMockFactory = new AccountMockFactory();


    @Test
    void findAllShouldReturnListOfAccounts() {
        List<Account> accounts = accountMockFactory.all();

        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountDTO> result = accountService.findAll();

        assertNotNull(result);
        assertEquals(accounts.size(), result.size());
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoAccountsFound() {
        when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        List<AccountDTO> result = accountService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldReturnAccountIfFound() {
        Account checkingAccount = accountMockFactory.createCheckingAccount();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(checkingAccount));

        AccountDTO result = accountService.findById(1L);

        assertEquals(checkingAccount.getAccountId(), result.getId());
        assertEquals(checkingAccount.getAlias(), result.getAlias());
        assertEquals(checkingAccount.getBalance(), result.getBalance());
    }

    @Test
    void findByIdShouldThrowResourceNotFoundWhenMissingAccount() {
        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.findById(5L));
    }

    @Test
    void saveAccountShouldReturnRecentlySavedAccount() {
        CreateAccount account = accountMapper
                .toCreateAccount(accountMockFactory.createMainAccount());


        when(accountRepository.save(any(Account.class)))
                .thenReturn(Optional.of(accountMockFactory.createMainAccount()));

        AccountDTO savedAccount = accountService.saveAccount(account);

        assertNotNull(savedAccount);
        log.info("Saved account: {}", savedAccount);
    }

    @Test
    void saveAccountShouldThrowExceptionIfDataIsMissing() {
        CreateAccount account = CreateAccount.builder().build();

        InvalidParametersException exception = assertThrows(InvalidParametersException.class, () ->
                accountService.saveAccount(account));

        assertEquals(3, exception.getErrorMessages().size());

        log.info("Error messages: {}", exception.getErrorMessages());
    }

    @Test
    void saveAccountShouldHandleDataNotCreatedExceptions() {
        CreateAccount account = CreateAccount.builder()
                .alias("Some Account")
                .type(AccountType.CHECKING)
                .balance(BigDecimal.TEN)
                .build();

        when(accountRepository.save(any(Account.class)))
                .thenReturn(Optional.empty());

        assertThrows(DataAccessException.class, () -> accountService.saveAccount(account));
    }

    @Test
    void deleteByIdShouldReturnAffectedRows() {
        when(accountRepository.deleteById(anyLong())).thenReturn(1);
        int result = accountService.deleteById(1L);
        assertEquals(1, result);
    }

    @Test
    void deleteByIdShouldThrowResourceNotFoundWhenAccountDoesNotExist() {
        when(accountRepository.deleteById(anyLong())).thenReturn(0);
        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteById(1L));
    }

    @Test
    void updateBalanceShouldReturnUpdatedAccount() {
        UpdateBalance req = UpdateBalance.builder().balance(BigDecimal.TEN).build();
        Account account = accountMockFactory.createMainAccount();
        account.setBalance(BigDecimal.TEN);

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.of(account));

        AccountDTO result = accountService.updateBalance(1L, req);

        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void updateBalanceShouldThrowExceptionIfDataIsMissing() {
        UpdateBalance req = UpdateBalance.builder().build();

        InvalidParametersException ex = assertThrows(InvalidParametersException.class, () ->
                accountService.updateBalance(null, req));

        assertEquals(2, ex.getErrorMessages().size());
    }

    @Test
    void updateBalanceShouldHandleDataNotUpdatedExceptions() {
        UpdateBalance req = UpdateBalance.builder().balance(BigDecimal.TEN).build();

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.empty());

        assertThrows(DataAccessException.class, () -> accountService.updateBalance(1L, req));

    }
}