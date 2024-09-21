package com.homs.account_rest_api.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        //
    }

    @Test
    public void findAllShouldReturnAListOfAccounts() {
        List<Account> expectedResult = Arrays.asList(new Account(), new Account());

        when(accountRepository.findAll()).thenReturn(expectedResult);

        List<Account> actualResult = accountService.findAll();

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult.size(), actualResult.size());
        verify(accountRepository, atMostOnce()).findAll();
    }

    @Test
    public void saveAccountShouldSaveNewAccountWhenRepositoryOk() {
        Account expectedResult = Account.builder()
                .accountId("f048c183-066c-4906-a0be-e5fe08dccd6b")
                .balance(BigDecimal.valueOf(10_000.00))
                .alias("New Account").build();

        when(accountRepository.save(any(Account.class)))
                .thenReturn(Optional.of(expectedResult));

        Account actualResult = accountService.saveAccount(expectedResult);

        assertEquals(expectedResult, actualResult);
        verify(accountRepository, atMostOnce()).save(eq(expectedResult));
    }

    @Test
    public void saveAccountShouldThrowExceptionWhenFails() {
        Account expectedResult = Account.builder()
                .accountId("f048c183-066c-4906-a0be-e5fe08dccd6b")
                .balance(BigDecimal.valueOf(10_000.00))
                .alias("New Account").build();

        when(accountRepository.save(eq(expectedResult)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotCreatedException.class, () ->
                accountService.saveAccount(expectedResult)
        );

        verify(accountRepository, atMostOnce()).save(eq(expectedResult));
    }

    @Test
    public void findByIdShouldReturnAnAccountOnSuccess() {
        String id = "f048c183-066c-4906-a0be-e5fe08dccd6b";
        Account expectedResult = Account.builder()
                .accountId("f048c183-066c-4906-a0be-e5fe08dccd6b")
                .balance(BigDecimal.valueOf(10_000.00))
                .alias("New Account").build();

        when(accountRepository.findById(eq(id)))
                .thenReturn(Optional.of(expectedResult));

        Account actualResult = accountService.findById(id);

        assertEquals(expectedResult, actualResult);
        verify(accountRepository, atMostOnce()).findById(eq(id));
    }

    @Test
    public void deleteByIdShouldReturnMoreThan1AffectedRow() {
        String id = "f048c183-066c-4906-a0be-e5fe08dccd6b";
        Integer expectedResult = 1;

        when(accountRepository.deleteById(eq(id)))
                .thenReturn(expectedResult);

        Integer actualResult = accountService.deleteById(id);

        assertEquals(expectedResult, actualResult);

        verify(accountRepository, atMostOnce()).deleteById(eq(id));
    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenFails() {
        String id = "aaaa-aaaa-aaaa";
        Integer expectedResult = 0;

        when(accountRepository.deleteById(eq(id)))
                .thenReturn(expectedResult);

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteById(id);
        });

        verify(accountRepository, atMostOnce()).deleteById(eq(id));
    }

    @Test
    public void updateBalanceShouldReturnAccountOnSuccess() {
        Account expectedResult = Account.builder()
                .accountId("f048c183-066c-4906-a0be-e5fe08dccd6b")
                .balance(BigDecimal.valueOf(10_000.00))
                .alias("New Account").build();

        when(accountRepository.updateBalance(eq(expectedResult)))
                .thenReturn(Optional.of(expectedResult));

        Account actualResult = accountService.updateBalance(expectedResult);

        assertEquals(expectedResult, actualResult);

        verify(accountRepository, atMostOnce())
                .updateBalance(eq(expectedResult));
    }

    @Test
    public void updateBalanceShouldThrowExceptionOnFailure() {
        Account expectedResult = Account.builder()
                .accountId("f048c183-066c-4906-a0be-e5fe08dccd6b")
                .balance(BigDecimal.valueOf(10_000.0050))
                .alias("New Account").build();

        when(accountRepository.updateBalance(eq(expectedResult)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                accountService.updateBalance(expectedResult)
        );

        verify(accountRepository, atMostOnce())
                .updateBalance(eq(expectedResult));
    }
}