package com.homs.account_rest_api.summary.service;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.mocks.*;
import com.homs.account_rest_api.summary.dto.CategoriesSummaryDto;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
@Slf4j
public class SummaryServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private SummaryService summaryService;

    private DummyAccounts dummyAccounts;
    private TransactionMockFactory transactionMockFactory;
    private CategoryMockFactory categoryMockFactory;
    private AccountMockFactory accountMockFactory;

    @Before
    public void setUp() {
        dummyAccounts = new DummyAccounts();
        transactionMockFactory = new TransactionMockFactory();
        categoryMockFactory = new CategoryMockFactory();
        accountMockFactory = new AccountMockFactory();
    }

    @Test
    public void shouldReturnGeneralSummary() {
        Account mainAccount = accountMockFactory.mainAccount();
        Account checkingAccount = accountMockFactory.checkingAccount();
        Category games = categoryMockFactory.gamesCategory();

        List<Transaction> mainAccountTransactions = transactionMockFactory
                .createMainAccountTransactions();

        List<Transaction> checkingAccountTransactions = new ArrayList<>(
                transactionMockFactory
                        .createCheckingAccountTransactions()
        );

        Transaction largestExpense = transactionMockFactory
                .createExpense(mainAccount, games, 10_000.0, "Laptop Gamer");

        checkingAccountTransactions.add(largestExpense);


        BigDecimal expectedBalance = mainAccount.getBalance().add(checkingAccount.getBalance());
        BigDecimal expectedCreditCardBalance = BigDecimal.ZERO;
        BigDecimal expectedSavingsGoal = BigDecimal.ZERO;
        TransactionDto expectedLargestExpense = transactionMapper.toTransactionDto(largestExpense);
        BigDecimal expectedTotalExpenses = Stream.concat(mainAccountTransactions.stream(), checkingAccountTransactions.stream())
                .filter(transaction -> transaction.getType().equals(TransactionType.EXPENSE))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Long expectedExpensesCount = Stream.concat(mainAccountTransactions.stream(), checkingAccountTransactions.stream())
                .filter(transaction -> transaction.getType().equals(TransactionType.EXPENSE))
                .count();

        when(accountRepository.findAll()).thenReturn(List.of(mainAccount, checkingAccount));

        when(transactionRepository.getAllTransactionsByMonth(
                eq(mainAccount.getAccountId()),
                any(Month.class),
                any(Year.class)
        )).thenReturn(mainAccountTransactions);

        when(transactionRepository.getAllTransactionsByMonth(
                eq(checkingAccount.getAccountId()),
                any(Month.class),
                any(Year.class)
        )).thenReturn(checkingAccountTransactions);

        Optional<SummaryDto> result = summaryService.getSummary();
        log.info(result.toString());

        assertTrue(result.isPresent());
        assertEquals(expectedBalance, result.get().getTotalBalance());
        assertEquals(expectedCreditCardBalance, result.get().getCreditCardExpenses());
        assertEquals(expectedSavingsGoal, result.get().getSavingsGoal());
        assertEquals(expectedLargestExpense, result.get().getLargestExpense());
        assertEquals(expectedExpensesCount.intValue(), result.get().getMetadata().getCount().intValue());
        assertEquals(expectedTotalExpenses, result.get().getMetadata().getTotalExpenses());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenNoAccounts() {
        when(accountRepository.findAll())
                .thenReturn(Collections.emptyList());

        Optional<SummaryDto> result = summaryService.getSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnZeroExpensesWhenNoExpenses() {
        BigDecimal expectedTotalBalance = BigDecimal.valueOf(120_000);
        Integer expectedExpensesCount = 0;

        when(accountRepository.findAll()).thenReturn(List.of(
                dummyAccounts.getMainAccount(),
                dummyAccounts.getCheckingAccount()
        ));

        Optional<SummaryDto> result = summaryService.getSummary();
        assertTrue(result.isPresent());

        assertEquals(expectedTotalBalance, result.get().getTotalBalance());
        assertEquals(BigDecimal.ZERO, result.get().getCreditCardExpenses());
        assertEquals(BigDecimal.ZERO, result.get().getSavingsGoal());
        assertNull(result.get().getLargestExpense());
        assertEquals(expectedExpensesCount, result.get().getMetadata().getCount());
        assertEquals(BigDecimal.ZERO, result.get().getMetadata().getTotalExpenses());
    }

    @Test
    public void shouldReturnCompleteCategorySummary() {
        Account mainAccount = accountMockFactory.mainAccount();
        Account checkingAccount = accountMockFactory.checkingAccount();

        List<Category> allCategories = categoryMockFactory.all();

        List<Transaction> mainAccountTransactions = transactionMockFactory
                .createMainAccountTransactions();
        List<Transaction> checkingAccountTransactions = transactionMockFactory
                .createCheckingAccountTransactions();

        BigDecimal expectedTotalExpenses = BigDecimal.valueOf(5_750.0);
        Integer expectedMetadataCount = 5;

        when(accountRepository.findAll()).thenReturn(List.of(
                mainAccount, checkingAccount
        ));

        when(transactionRepository.getAllTransactionsByMonth(
                eq(mainAccount.getAccountId()),
                any(Month.class),
                any(Year.class)
        )).thenReturn(mainAccountTransactions);

        when(transactionRepository.getAllTransactionsByMonth(
                eq(checkingAccount.getAccountId()),
                any(Month.class),
                any(Year.class)
        )).thenReturn(checkingAccountTransactions);

        when(categoryRepository.getAllCategories())
                .thenReturn(allCategories);

        Optional<CategoriesSummaryDto> result = summaryService.getCategoriesSummary();
        assertFalse(result.isEmpty());
        assertEquals(expectedMetadataCount, result.get().getMetadata().getCount());
        assertEquals(expectedTotalExpenses, result.get().getMetadata().getTotalExpenses());
        log.info(result.get().toString());
    }

    @Test
    public void shouldReturnEmptyWhenNoAccountsForCategorySummary() {

        when(accountRepository.findAll())
                .thenReturn(Collections.emptyList());

        Optional<CategoriesSummaryDto> result = summaryService.getCategoriesSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyCategorySummaryWhenNoExpenses() {
        Account noExpenses = accountMockFactory.accountWithoutExpenses();
        when(accountRepository.findAll())
                .thenReturn(Collections.singletonList(noExpenses));

        when(transactionRepository.getAllTransactionsByMonth(
                anyString(),
                any(Month.class),
                any(Year.class)
        )).thenReturn(Collections.emptyList());

        when(categoryRepository.getAllCategories())
                .thenReturn(categoryMockFactory.all());

        BigDecimal expectedTotal = BigDecimal.ZERO;
        Integer expectedExpensesCount = 0;
        Optional<CategoriesSummaryDto> result = summaryService.getCategoriesSummary();
        log.info(result.toString());
        assertTrue(result.isPresent());
        result.get().getCategories()
                .forEach(categorySummaryDto ->
                        assertEquals(expectedTotal, categorySummaryDto.getTotal()));
        assertEquals(expectedTotal, result.get().getMetadata().getTotalExpenses());
        assertEquals(expectedExpensesCount, result.get().getMetadata().getCount());
    }
}