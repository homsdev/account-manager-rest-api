package com.homs.account_rest_api.accounts.service;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountMockFactory accountMockFactory;
    private List<Account> accountsMockList;


    @Before
    public void setUp() {
        accountMockFactory = new AccountMockFactory();
        accountsMockList = List.of(
                accountMockFactory.mainAccount(),
                accountMockFactory.checkingAccount()
        );
    }

    @Test
    public void findAllShouldReturnAllAccounts() {
        when(accountRepository.findAll())
                .thenReturn(accountsMockList);

        List<AccountDTO> result = accountService.findAll();
        assertEquals(accountsMockList.size(), result.size());
    }

    @Test
    public void findAllShouldReturnEmptyListIfNoAccounts() {
        when(accountRepository.findAll())
                .thenReturn(Collections.emptyList());
        List<AccountDTO> result = accountService.findAll();
        assertTrue(result.isEmpty());
    }


    @Test
    public void findByIdShouldReturnAccountIFExists() {
        Account mainAccount = accountMockFactory.mainAccount();
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mainAccount));

        AccountDTO result = accountService.findById("acc-main");

        log.info(result.toString());

        assertNotNull(result);
        assertEquals(mainAccount.getAccountId(), result.getId());
        assertEquals(mainAccount.getBalance(), result.getBalance());
        assertEquals(mainAccount.getAlias(), result.getAlias());
    }

    @Test
    public void findByIdShouldThrowExceptionIfNoAccountFound() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.findById("acc-invalid"));
    }

    @Test
    public void saveAccountShouldReturnCreatedAccountOnSuccess() {
        Account checking = accountMockFactory.checkingAccount();
        AccountDTO checkingDTO = AccountDTO.builder()
                .alias(checking.getAlias())
                .balance(checking.getBalance())
                .build();
        when(accountRepository.save(any(Account.class)))
                .thenReturn(Optional.of(checking));

        AccountDTO result = accountService.saveAccount(checkingDTO);
        log.info(result.toString());
        assertEquals(checking.getAlias(), result.getAlias());
        assertEquals(checking.getBalance(), result.getBalance());
        assertEquals(checking.getAccountId(), result.getId());
    }

    @Test
    public void saveAccountShouldThrowExceptionWhenMissingAccount() {
        AccountDTO emptyAccount = AccountDTO.builder().build();
        AccountDTO accountWithBlankAlias = AccountDTO.builder().alias("    ").build();
        AccountDTO accountWithLongAlias = AccountDTO.builder()
                .alias("xgkXLf5cCPeLnjK1miA8nMyJFQH73Z4HDQryt1VuWwZ2HEt6NAr3CZXigwW9").build();
        AccountDTO accountWithNotAllowedAlias = AccountDTO.builder().alias("%&$").build();
        AccountDTO accountWithIncorrectBalance = AccountDTO.builder()
                .alias("dummy").balance(BigDecimal.valueOf(100.755)).build();

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(null)
        );

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(emptyAccount));

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(accountWithBlankAlias));

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(accountWithLongAlias));

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(accountWithNotAllowedAlias));

        assertThrows(InvalidParametersException.class,
                () -> accountService.saveAccount(accountWithIncorrectBalance));

        verifyNoInteractions(accountRepository);
    }

    @Test
    public void saveAccountShouldThrowExceptionWhenFailsToCreate() {
        AccountDTO mainAccountDTO = AccountMapper.toDTO(accountMockFactory.mainAccount());
        when(accountRepository.save(any(Account.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotCreatedException.class,
                () -> accountService.saveAccount(mainAccountDTO)
        );
    }

    @Test
    public void deleteByIdShouldReturnInteger() {
        when(accountRepository.deleteById(anyString()))
                .thenReturn(1);

        Integer result = accountService.deleteById("acc-main");

        assertEquals(1, result.intValue());
    }

    @Test
    public void deleteByIdShouldThrowErrorWhenInvalidId() {
        assertThrows(InvalidParametersException.class,
                () -> accountService.deleteById("  "));

        assertThrows(InvalidParametersException.class,
                () -> accountService.deleteById(null));
    }

    @Test
    public void updateBalanceShouldUpdateAccountBalance() {
        Account account = Account.builder()
                .accountId("acc-zero")
                .alias("zero balance")
                .balance(BigDecimal.valueOf(5_000))
                .build();

        AccountDTO dto = AccountMapper.toDTO(account);

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.of(account));

        AccountDTO result = accountService.updateBalance("id", dto);

        assertEquals(account.getBalance(), result.getBalance());
        assertEquals(account.getAccountId(), result.getId());
        assertEquals(account.getAlias(), result.getAlias());
    }

    @Test
    public void updateBalanceShouldThrowExceptionWithInvalidParams() {
        assertThrows(InvalidParametersException.class,
                () -> accountService.updateBalance(null, null));
        verifyNoInteractions(accountRepository);
    }

    @Test
    public void updateBalanceShouldThrowExceptionWithBlankIdAndMissingBalance() {
        AccountDTO incompleteDTO = AccountDTO.builder().build();
        assertThrows(InvalidParametersException.class,
                () -> accountService.updateBalance("  ", incompleteDTO));
        verifyNoInteractions(accountRepository);
    }

    @Test
    public void updateBalanceShouldThrowExceptionIfAccountDoesNotExist() {
        AccountDTO anyDto = AccountDTO.builder()
                .balance(BigDecimal.valueOf(1200.00))
                .build();
        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> accountService.updateBalance("invalid-id", anyDto));
        assertTrue(true);
    }
}