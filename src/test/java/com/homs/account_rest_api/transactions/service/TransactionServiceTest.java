package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import com.homs.account_rest_api.mocks.TransactionMockFactory;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionJPARepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionJPARepositoryImpl transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionMockFactory transactionMockFactory;
    private AccountMockFactory accountMockFactory;
    private CategoryMockFactory categoryMockFactory;

    @Before
    public void setUp() {
        transactionMockFactory = new TransactionMockFactory();
        accountMockFactory = new AccountMockFactory();
        categoryMockFactory = new CategoryMockFactory();
    }

    @Test
    public void saveTransactionShouldThrowExceptionWhenInvalidAccountId() {
        CreateTransactionRequest req = CreateTransactionRequest.builder().build();
        assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(req, "   "));
        assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(req, null));
    }

    @Test
    public void saveTransactionShouldThrowExceptionWithNullOrBlankValues() {
        CreateTransactionRequest nullRequest = CreateTransactionRequest.builder().build();
        CreateTransactionRequest invalidRequest = CreateTransactionRequest.builder()
                .type("  ")
                .date("  ")
                .alias("  ")
                .categoryId("  ")
                .build();
        List<String> nullErrorMessages = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(nullRequest, "valid-id")).getErrorMessages();
        log.info(nullErrorMessages.toString());
        assertEquals(5, nullErrorMessages.size());
        List<String> invalidErrorMessages = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(invalidRequest, "valid-id")).getErrorMessages();
        log.info(invalidErrorMessages.toString());
        assertEquals(5, invalidErrorMessages.size());

        assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(null, "valid-id"));
    }

    @Test
    public void saveTransactionShouldThrowExceptionWithInvalidAmounts() {
        CreateTransactionRequest expenseRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(-1200.75))
                .alias("Random Expense")
                .date("05-07-2025")
                .type("EXPENSE")
                .categoryId("cat-games")
                .build();

        CreateTransactionRequest incomeRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(-1200.75))
                .type("INCOME")
                .alias("Random Income")
                .date("05-07-2025")
                .categoryId("cat-games")
                .build();

        List<String> expenseReqErrors = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(expenseRequest, "valid-id")).getErrorMessages();
        log.info(expenseReqErrors.toString());
        assertEquals(1, expenseReqErrors.size());
        List<String> incomeReqErrors = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(incomeRequest, "valid-id")).getErrorMessages();
        log.info(incomeReqErrors.toString());
        assertEquals(1, incomeReqErrors.size());
    }

    @Test
    public void saveTransactionShouldThrowExceptionWithIncorrectType() {
        CreateTransactionRequest expenseRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(1200.75))
                .alias("Random Expense")
                .date("05-07-2025")
                .type("INVALID-TYPE")
                .categoryId("cat-games")
                .build();

        InvalidParametersException errors = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(expenseRequest, "account-id"));

        log.info(errors.getErrorMessages().toString());
    }

    @Test
    public void saveTransactionShouldThrowExceptionWithInvalidDate() {
        CreateTransactionRequest expenseRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(1200.75))
                .alias("Random Expense")
                .date("05-15-2025")
                .type("EXPENSE")
                .categoryId("cat-games")
                .build();

        InvalidParametersException results = assertThrows(InvalidParametersException.class,
                () -> transactionService.saveTransaction(expenseRequest, "account-id"));
        log.info(results.getErrorMessages().toString());
    }

    @Test
    public void saveTransactionShouldThrowNotFoundWhenAccountOrCategoryNotExists() {
        CreateTransactionRequest expenseRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(1200.75))
                .alias("Random Expense")
                .date("05-08-2025")
                .type("EXPENSE")
                .categoryId("cat-games")
                .build();

        when(accountRepository.findById("not-existent-account-id"))
                .thenReturn(Optional.empty());

        when(accountRepository.findById("valid-accountId"))
                .thenReturn(Optional.of(accountMockFactory.mainAccount()));

        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.empty());

        String missingAccountMsg = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.saveTransaction(expenseRequest, "not-existent-account-id"))
                .getMessage();
        log.info(missingAccountMsg);

        String missingCategoryMsg = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.saveTransaction(expenseRequest, "valid-accountId")).getMessage();
        log.info(missingCategoryMsg);
    }

    @Test
    public void saveTransactionShouldThrowExceptionWhenResourceNotCreated() {
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(15_000))
                .type("EXPENSE")
                .date("05-08-2025")
                .alias("Some Expense")
                .categoryId("cat-transportation")
                .build();

        Account mainAccount = accountMockFactory.mainAccount();
        Category transportation = categoryMockFactory.transportation();

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mainAccount));

        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.of(transportation));

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotCreatedException.class,
                () -> transactionService.saveTransaction(req, "valid-id"));
    }

    @Test
    public void saveTransactionShouldSaveExpenseTransaction() {
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(15_000))
                .type("EXPENSE")
                .date("05-08-2025")
                .alias("Some Expense")
                .categoryId("cat-transportation")
                .build();

        Account mainAccount = accountMockFactory.mainAccount();
        Category transportation = categoryMockFactory.transportation();


        Transaction expense = transactionMockFactory.createExpense(
                mainAccount,
                transportation,
                15_000.00,
                "Some Expense"
        );

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(accountMockFactory.mainAccount()));

        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.of(categoryMockFactory.transportation()));

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.of(expense));

        TransactionDto savedTransaction = transactionService
                .saveTransaction(req, "main-account-id");

        log.info(savedTransaction.toString());

        assertEquals(
                BigDecimal.valueOf(35_000.0),
                savedTransaction.getAccountDTO().getBalance()
        );
    }

    @Test
    public void saveTransactionShouldSaveIncomeTransaction() {
        CreateTransactionRequest req = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(15_000))
                .type("INCOME")
                .date("05-08-2025")
                .alias("Some Income")
                .categoryId("cat-salary")
                .build();

        Account mainAccount = accountMockFactory.mainAccount();
        Category salary = categoryMockFactory.salary();


        Transaction income = transactionMockFactory.createIncome(
                mainAccount,
                salary,
                15_000.00,
                "Some Income"
        );

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(accountMockFactory.mainAccount()));

        when(categoryRepository.getCategory(anyString()))
                .thenReturn(Optional.of(categoryMockFactory.salary()));

        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.of(income));

        TransactionDto savedTransaction = transactionService
                .saveTransaction(req, "main-account-id");

        log.info(savedTransaction.toString());

        assertEquals(
                BigDecimal.valueOf(65_000.0),
                savedTransaction.getAccountDTO().getBalance()
        );
    }


    @Test
    public void getAllTransactionsByMonthAndYearShouldThrowExceptionWithInvalidParams() {
        String invalidMonth = "Augustus";
        String invalidNumericYear = "-9999";
        String invalidNonNumericYear = "ABDC";
        String validMonth = "may";
        String validYear = "2025";
        String invalidMonthMsg = assertThrows(InvalidParametersException.class,
                () -> transactionService.getAllTransactionsByMonthAndYear(invalidMonth,
                        validYear, "valid-acc-id")).getErrorMessages().toString();
        log.info(invalidMonthMsg);
        String invalidNumericYearMsg = assertThrows(InvalidParametersException.class,
                () -> transactionService.getAllTransactionsByMonthAndYear(validMonth,
                        invalidNumericYear, "valid-acc-id")).getErrorMessages().toString();
        log.info(invalidNumericYearMsg);
        String invalidadNonNumericYear = assertThrows(InvalidParametersException.class,
                () -> transactionService.getAllTransactionsByMonthAndYear(validMonth,
                        invalidNonNumericYear, "valid-acc-id")).getErrorMessages().toString();
        log.info(invalidadNonNumericYear);
    }

    @Test
    public void getAllTransactionsByMonthAndYearShouldSetMonthAndYearToCurrentWithNullOrBlankValues() {
        Account mainAccount = accountMockFactory.mainAccount();
        String blankMonth = "  ";
        String blankYear = "  ";
        Month currentMonth = LocalDate.now().getMonth();
        Year currentYear = Year.now();

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(mainAccount));

        transactionService.getAllTransactionsByMonthAndYear(blankMonth, blankYear, "valid-id");

        verify(transactionRepository).getAllTransactionsByMonth(
                anyString(),
                eq(currentMonth),
                eq(currentYear)
        );
    }

    @Test
    public void getAllTransactionsByMonthAndYearShouldThrowExceptionWithInvalidAccountId() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService
                        .getAllTransactionsByMonthAndYear("MAY", "2025", "invalid-id"));
    }
}
