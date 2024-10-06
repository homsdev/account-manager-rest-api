package com.homs.account_rest_api.service;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.repository.AccountRepository;
import com.homs.account_rest_api.repository.TransactionRepository;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    private final Account mockAccount = Account.builder()
            .accountId(UUID.randomUUID().toString())
            .alias("MockAccount")
            .balance(BigDecimal.valueOf(10_000.50))
            .build();

    private final Transaction mockTransactionA = Transaction.builder()
            .transactionId(UUID.randomUUID().toString())
            .date(LocalDate.of(2024, 10, 15))
            .type(TransactionType.EXPENSE)
            .amount(BigDecimal.valueOf(750.99))
            .account(mockAccount)
            .alias("DISNEY+")
            .build();

    private final Transaction incomeTransactionMock = Transaction.builder()
            .transactionId(UUID.randomUUID().toString())
            .date(LocalDate.of(2024, 10, 30))
            .type(TransactionType.INCOME)
            .amount(BigDecimal.valueOf(1200.50))
            .account(mockAccount)
            .alias("SALARY")
            .build();

    @Test
    public void saveTransactionShouldReturnTransactionWhenSaveExpenseOK() {
        BigDecimal expectedBalance = mockAccount.getBalance()
                .subtract(mockTransactionA.getAmount());

        Account updatedAccount = Account.builder()
                .accountId(mockAccount.getAccountId())
                .balance(expectedBalance)
                .alias(mockAccount.getAlias())
                .build();

        when(transactionRepository.saveTransaction(eq(mockTransactionA)))
                .thenReturn(Optional.of(mockTransactionA));

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mockAccount));

        when(accountRepository.updateBalance(eq(mockAccount)))
                .thenReturn(Optional.of(updatedAccount));

        Transaction transaction = transactionService.saveTransaction(mockTransactionA, mockAccount.getAccountId());


        assertNotNull(transaction);
        assertEquals(mockTransactionA, transaction);
        assertEquals(mockTransactionA.getAccount(), transaction.getAccount());

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateBalance(argumentCaptor.capture());

        Account capturedValue = argumentCaptor.getValue();

        assertEquals("Account balance was correctly modified"
                , expectedBalance, capturedValue.getBalance());
    }

    @Test
    public void saveTransactionShouldReturnTransactionWhenSaveIncomeOK() {
        BigDecimal expectedBalance = mockAccount.getBalance()
                .add(incomeTransactionMock.getAmount());

        Account updatedAccount = Account.builder()
                .accountId(mockAccount.getAccountId())
                .balance(expectedBalance)
                .alias(mockAccount.getAlias())
                .build();

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mockAccount));

        when(accountRepository.updateBalance(eq(mockAccount)))
                .thenReturn(Optional.of(updatedAccount));

        when(transactionRepository.saveTransaction(eq(incomeTransactionMock)))
                .thenReturn(Optional.of(incomeTransactionMock));

        Transaction transaction = transactionService
                .saveTransaction(incomeTransactionMock, mockAccount.getAccountId());

        assertNotNull(transaction);
        assertEquals(incomeTransactionMock, transaction);
        assertEquals(incomeTransactionMock.getAccount(), transaction.getAccount());

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateBalance(argumentCaptor.capture());

        Account capturedValue = argumentCaptor.getValue();

        assertEquals("Account balance was correctly modified"
                , expectedBalance, capturedValue.getBalance());

        System.out.println(capturedValue);
        System.out.println(transaction);
    }

    @Test
    public void saveTransactionShouldThrowErrorWhenFails() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mockAccount));

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.of(mockAccount));

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotCreatedException.class, () ->
                transactionService.saveTransaction(mockTransactionA, mockAccount.getAccountId())
        );
    }

    @Test
    public void saveTransactionShouldThrownErrorWhenUpdateBalanceFails() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mockAccount));

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.empty());

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.saveTransaction(mockTransactionA, mockAccount.getAccountId());
        });
    }

    @Test
    public void saveTransactionShouldThrownErrorWhenFindAccountByIdFails() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.saveTransaction(mockTransactionA, mockAccount.getAccountId())
        );

        verify(transactionRepository, never()).saveTransaction(any(Transaction.class));
    }
}