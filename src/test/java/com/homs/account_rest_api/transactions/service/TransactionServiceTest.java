package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.mocks.DummyTransactions;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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
    private DummyTransactions dummyTransactions;

    @Before
    public void setUp() {
        dummyTransactions = new DummyTransactions();
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

        String accountId = sampleAccount.getAccountId();

        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.saveTransaction(sampleTransaction, accountId)
        );

        verify(accountRepository, never()).updateBalance(any());
        verify(transactionRepository, never()).saveTransaction(any());
    }

    /**
     * Validate that every registered income adds to linked account balance
     */
    @Test
    public void assertIncomeAddsToAccountBalance() {
        BigDecimal expectedBalance = BigDecimal.valueOf(23_251.25);
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(sampleAccount));

        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.of(sampleAccount));

        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleIncomeTransaction));

        transactionService.saveTransaction(sampleIncomeTransaction, sampleAccount.getAccountId());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateBalance(accountCaptor.capture());

        assertEquals("Account balance was not updated correctly"
                , expectedBalance, accountCaptor.getValue().getBalance());
    }

    /**
     * Validate that every registered expense subtracts from linked account
     */
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

    /**
     * Validates rollback if account balance update operation fails
     */
    @Test
    public void assertTransactionRollBackWhenUpdateBalanceFail() {
        when(accountRepository.findById(any()))
                .thenReturn(Optional.of(sampleAccount));
        when(accountRepository.updateBalance(any()))
                .thenReturn(Optional.empty());
        when(transactionRepository.saveTransaction(any()))
                .thenReturn(Optional.of(sampleTransaction));

        String accountId = sampleAccount.getAccountId();

        assertThrows(ResourceNotFoundException.class, () -> transactionService.saveTransaction(sampleTransaction, accountId));

        verify(transactionRepository, never()).saveTransaction(any());
    }

    /**
     * Validates saveTransaction throws TransactionInvalidData("Missing account info");
     * When account info is missing
     */
    @Test
    public void shouldThrowExceptionWhenMissingAccountData() {
        assertThrows(TransactionInvalidData.class,
                () -> transactionService.saveTransaction(sampleTransaction, null));
    }

    /**
     * Should Throw TransactionInvalidData
     * When transaction info is missing
     */
    @Test
    public void shouldThrowExceptionWhenMissingTransactionData() {
        assertThrows(TransactionInvalidData.class,
                () -> transactionService.saveTransaction(null, "accountId"));
    }

    /**
     * getAllTransactionsByMonthAndYear() Should return list of valid data
     * When called with any month and year
     */
    @Test
    public void getAllTransactionsByMonthAndYearShouldReturnListOfTransactions() {
        Transaction services = dummyTransactions.getServices();
        Transaction groceries = dummyTransactions.getGroceries();
        Transaction salary = dummyTransactions.getSalary();

        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(sampleAccount));

        when(transactionRepository.getAllTransactionsByMonth(anyString(), any(Month.class), any(Year.class)))
                .thenReturn(List.of(services,groceries,salary));

        List<Transaction> result = transactionService.getAllTransactionsByMonthAndYear(
                Month.APRIL, Year.now(), "accountId"
        );

        log.info(String.valueOf(result.get(0)));
        assertEquals(3, result.size());
    }

    /**
     * getAllTransactionsByMonthAndYear() should throw ResourceNotFoundException
     * When passed invalid accountId
     */
    @Test()
    public void shouldThrowResourceNotFoundExceptionWhenInvalidAccount() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        Year currentYear = Year.now();

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getAllTransactionsByMonthAndYear(
                Month.JUNE, currentYear, ""
        ));
    }

    /**
     * getAllTransactionsByMonthAndYear() should return EmptyList
     * When there is no transactions found for the given period
     */
    @Test
    public void getAllTransactionsByMonthAndYearShouldReturnEmptyListIfCurrentAccountDoesNoHaveTransactions() {
        when(accountRepository.findById(anyString()))
                .thenReturn(Optional.of(sampleAccount));

        Year currentYear = Year.now();

        List<Transaction> transactions =
                transactionService.getAllTransactionsByMonthAndYear(Month.AUGUST, currentYear, "accountId");

        assertTrue(transactions.isEmpty());
    }

    /**
     * Happy path
     */
    @Test
    public void loadTransactionsShouldReadCSVContent() {
        String csvContent = """
                Amount,Type,Date,Account,Alias,categoryId
                1000.50,INCOME,2024-04-15,ACC12345,Salary,categoryId
                -150.75,EXPENSE,2024-04-16,ACC67890,Grocery,categoryId
                250.00,INCOME,2024-04-17,ACC12345,Freelance,categoryId
                -50.00,EXPENSE,2024-04-18,ACC67890,Utilities,categoryId
                """;
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );
        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.of(sampleTransaction));
        List<Transaction> loadedTransactions = transactionService.loadTransactions(mockFile);
        assertEquals(4, loadedTransactions.size());
    }


    @Test
    public void loadTransactionsShouldSkipEmptyLines() {
        String csvContent = """
                Amount,Type,Date,Account,Alias
                                
                """;
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );
        when(transactionRepository.saveTransaction(any(Transaction.class)))
                .thenReturn(Optional.of(sampleTransaction));
        List<Transaction> loadedTransactions = transactionService.loadTransactions(mockFile);
        assertEquals(0, loadedTransactions.size());
    }

    @Test
    public void loadTransactionsShouldThrowErrWithAnEmptyFile() {
        String csvContent = "";
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        assertThrows(
                InvalidParametersException.class,
                () -> transactionService.loadTransactions(mockFile));
    }

    @Test
    public void loadTransactionsShouldThrowIOExceptionWhenCorruptedFile() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.getInputStream())
                .thenThrow(new IOException());

        assertThrows(InvalidParametersException.class,
                () -> transactionService.loadTransactions(mockFile));
    }
}