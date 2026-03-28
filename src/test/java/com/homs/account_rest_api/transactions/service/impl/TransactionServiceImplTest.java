package com.homs.account_rest_api.transactions.service.impl;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import com.homs.account_rest_api.mocks.TransactionMock;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import com.homs.account_rest_api.transactions.service.BalanceCalculator;
import com.homs.account_rest_api.transactions.validations.TransactionRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Spy
    TransactionMapper transactionMapper;

    @Spy
    TransactionRequestValidator requestValidator;

    @Spy
    BalanceCalculator balanceCalculator;

    @InjectMocks
    TransactionServiceImpl transactionService;

    private final AccountMockFactory accountMockFactory = new AccountMockFactory();
    private final CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
    private final TransactionMapper transactionMapperHelper = new TransactionMapper();

    @Test
    void createTransactionShouldCreateTransaction() {
        CreateTransaction createTransaction = CreateTransaction.builder()
                .amount(BigDecimal.valueOf(50_000))
                .type("INCOME")
                .date("05-05-2024")
                .alias("Test transaction")
                .categoryId(1L)
                .accountId(1L)
                .build();
        Account account = accountMockFactory.createMainAccount();
        Category category = categoryMockFactory.createGamesCategory();
        Transaction expectedTransaction = transactionMapperHelper.toEntity(createTransaction);
        TransactionDto expectedTransactionDto = transactionMapperHelper.toTransactionDto(expectedTransaction);

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(categoryRepository.getCategory(anyLong()))
                .thenReturn(Optional.of(category));

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.of(expectedTransaction));

        when(accountRepository.updateBalance(any(Account.class)))
                .thenReturn(Optional.of(account));

        TransactionDto result = transactionService.createTransaction(createTransaction);

        assertNotNull(result);
        assertEquals(expectedTransactionDto.getAmount(), result.getAmount());
        assertEquals(expectedTransactionDto.getDate(), result.getDate());
        assertEquals(expectedTransactionDto.getDescription(), result.getDescription());
        assertEquals(expectedTransactionDto.getType(), result.getType());
    }

    @Test
    void createTransactionShouldThrowExceptionWhenRelatedAccountIsNotFound() {
        CreateTransaction req = CreateTransaction.builder()
                .amount(BigDecimal.valueOf(50_000))
                .type("INCOME")
                .date("05-05-2024")
                .alias("Test transaction")
                .categoryId(1L)
                .accountId(1L)
                .build();


        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.createTransaction(req));

        verify(accountRepository, atLeastOnce()).findById(anyLong());
        verify(categoryRepository, never()).getCategory(anyLong());
        verify(transactionRepository, never()).saveTransaction(any(Transaction.class));
        verify(accountRepository, never()).updateBalance(any(Account.class));

    }

    @Test
    void createTransactionShouldThrowExceptionWhenRelatedCategoryIsNotFound() {
        CreateTransaction req = CreateTransaction.builder()
                .amount(BigDecimal.valueOf(50_000))
                .type("INCOME")
                .date("05-05-2024")
                .alias("Test transaction")
                .categoryId(1L)
                .accountId(1L)
                .build();

        Account account = accountMockFactory.createMainAccount();

        when(accountRepository.findById(anyLong()))
                .thenReturn(Optional.of(account));

        when(categoryRepository.getCategory(anyLong()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.createTransaction(req));

        verify(accountRepository, atLeastOnce()).findById(anyLong());
        verify(categoryRepository, atLeastOnce()).getCategory(anyLong());
        verify(transactionRepository, never()).saveTransaction(any(Transaction.class));
        verify(accountRepository, never()).updateBalance(any(Account.class));
    }

    @Test
    void findTransactionsByAccountAndMonthShouldReturnTransactions() {
        Transaction gamesTrx = TransactionMock.withCategoryAndAmount("Games", BigDecimal.valueOf(1000));
        Transaction foodTrx = TransactionMock.withCategoryAndAmount("Food", BigDecimal.valueOf(2000));
        Transaction transportationTrx = TransactionMock
                .withCategoryAndAmount("Transportation", BigDecimal.valueOf(3000));

        DateFilterTransactionReq req = DateFilterTransactionReq.builder()
                .accountId(1L)
                .month(1)
                .year(2023)
                .build();

        when(transactionRepository.getAllTransactionsByMonth(
                anyLong(), any(Month.class), any(Year.class)
        )).thenReturn(List.of(gamesTrx, foodTrx, transportationTrx));

        List<TransactionDto> transactions = transactionService.findTransactionsByAccountAndMonth(req);
        assertEquals(3, transactions.size());
    }

    @Test
    void findTransactionsByAccountAndMonthShouldReturnEmptyCollectionWhenNoTransactionsFound() {
        DateFilterTransactionReq req = DateFilterTransactionReq.builder().accountId(1L).month(1).year(2023).build();

        when(transactionRepository.getAllTransactionsByMonth(
                anyLong(), any(Month.class), any(Year.class)
        )).thenReturn(Collections.emptyList());

        List<TransactionDto> transactions = transactionService
                .findTransactionsByAccountAndMonth(req);
        assertTrue(transactions.isEmpty());
    }
}