package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.repository.AccountRepository;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class TransactionServiceTest {

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    private Account sampleAccount;
    private Transaction sampleTransaction;
    private Transaction sampleIncomeTransaction;
    private final BigDecimal accountBalanceTest = BigDecimal.valueOf(10_500.50);

    @Before
    public void setUp() throws Exception {
        sampleAccount = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .alias("Account A")
                .balance(accountBalanceTest)
                .build();
        sampleTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .date(LocalDate.of(2024, 10, 15))
                .type(TransactionType.EXPENSE)
                .amount(BigDecimal.valueOf(750.99))
                .account(sampleAccount)
                .alias("Streaming Service")
                .build();
        sampleIncomeTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .date(LocalDate.of(2024, 10, 15))
                .type(TransactionType.INCOME)
                .amount(BigDecimal.valueOf(12_750.75))
                .account(sampleAccount)
                .alias("Payment")
                .build();
    }

    /**
     * Asserts that ResourceNotFoundException is thrown when an invalid account is
     * linked to the transaction to be stored
     */
    @Test
    public void assertResourceNotFoundExceptionIsThrow() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.saveTransaction(sampleTransaction, sampleAccount.getAccountId())
        );

        verify(accountRepository, never()).updateBalance(any());
        verify(transactionRepository, never()).saveTransaction(any());
    }

    @Test
    public void assertIncomeAddsToAccountBalance() {
        BigDecimal expectedBalance = BigDecimal.valueOf(23_251.25);
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(sampleAccount));

        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.of(sampleAccount));

        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleIncomeTransaction));

        //Execution
        transactionService.saveTransaction(sampleIncomeTransaction, sampleAccount.getAccountId());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateBalance(accountCaptor.capture());

        assertEquals("Account balance was not updated correctly"
                , expectedBalance, accountCaptor.getValue().getBalance());
    }

    @Test
    public void assertExpenseSubtractsToAccountBalance() {
        BigDecimal expectedBalance = BigDecimal.valueOf(9_749.51);
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(sampleAccount));
        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleTransaction));

        transactionService.saveTransaction(sampleTransaction, sampleAccount.getAccountId());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateBalance(accountCaptor.capture());

        assertEquals(expectedBalance, accountCaptor.getValue().getBalance());
    }

    @Test
    public void assertTransactionRollBackWhenUpdateBalanceFail() {
        when(accountRepository.findById(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleTransaction));

        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.saveTransaction(sampleTransaction, sampleAccount.getAccountId());
        });

        verify(transactionRepository, never()).saveTransaction(any());
    }

    @Test(expected = TransactionInvalidData.class)
    public void assertSaveTransactionThrowsExceptionWhenReceiveNullValue() {
        when(accountRepository.findById(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleTransaction));
        transactionService.saveTransaction(null, null);
    }

    @Test(expected = TransactionInvalidData.class)
    public void assertSaveTransactionThrowsExceptionWhenReqPropertiesAreNull() {
        when(accountRepository.findById(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleTransaction));
        transactionService.saveTransaction(
                Transaction.builder().build(),
                null);
    }
}