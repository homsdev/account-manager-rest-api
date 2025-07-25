package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.mocks.DummyTransactions;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.transactions.mapper.TransactionRowMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class TransactionMysqlRepositoryTest {
    private Transaction sampleTransaction;

    @MockBean
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    private DummyTransactions dummyTransactions;

    @Before
    public void setUp() {
        dummyTransactions = new DummyTransactions();
        Account sampleAccount = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .alias("sampleAccount")
                .balance(BigDecimal.valueOf(10_000.50))
                .build();
        sampleTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .date(LocalDate.of(2024, 10, 15))
                .type(TransactionType.EXPENSE)
                .amount(BigDecimal.valueOf(750.99))
                .account(sampleAccount)
                .alias("DISNEY+")
                .build();
    }

    /**
     * Test to assert when update operation success it returns 1 row affected
     */
    @Test()
    public void shouldReturnOptionalWithTransactionWhenOk() {
        when(jdbcTemplate.update(anyString(), anyMap()))
                .thenReturn(1);

        Optional<Transaction> transaction = transactionRepository.saveTransaction(dummyTransactions.getGroceries());
        assertFalse(transaction.isEmpty());

        Transaction actualResult = transaction.get();

        assertEquals(TransactionType.EXPENSE, actualResult.getType());
    }

    /**
     * Test to assert when update operation fails it returns 0 rows affected
     */
    @Test
    public void saveTransactionShouldReturnEmptyOptionalWhenFails() {
        when(jdbcTemplate.update(anyString(), anyMap()))
                .thenReturn(0);

        Optional<Transaction> transaction = transactionRepository.saveTransaction(dummyTransactions.getGroceries());

        assertTrue(transaction.isEmpty());
    }

    @Test(expected = TransactionInvalidData.class)
    public void shouldThrowErrorWhenATransactionPropertyIsInvalid() {
        Transaction dummy = Transaction.builder()
                .build();
        transactionRepository.saveTransaction(dummy);
    }

    @Test(expected = TransactionInvalidData.class)
    public void shouldThrowErrorWhenTransactionIsNull() {
        transactionRepository.saveTransaction(null);
    }

    @Test
    public void getAllTransactionsByMonth_shouldReturnRetrievedTransactionFromDB() {

        Transaction a = Transaction.builder().build();
        Transaction b = Transaction.builder().build();
        Transaction c = Transaction.builder().build();
        List<Transaction> expectedTransactions = List.of(a, b, c);

        Map<String, Object> expectedQueryParams = new HashMap<>();
        expectedQueryParams.put("accountId", "sampleId");
        expectedQueryParams.put("year", 2025);
        expectedQueryParams.put("month", 12);

        when(jdbcTemplate.query(anyString(), anyMap(), any(TransactionRowMapper.class)))
                .thenReturn(expectedTransactions);
        List<Transaction> result = transactionRepository
                .getAllTransactionsByMonth("sampleId", Month.DECEMBER, Year.now());
        assertEquals(3, result.size());
        verify(jdbcTemplate).query(anyString(), eq(expectedQueryParams), any(TransactionRowMapper.class));
    }

    @Test
    public void getAllTransactionsByMonth_shouldReturnEmptyListWithNoSelectedAccount() {
        List<Transaction> result = transactionRepository
                .getAllTransactionsByMonth("", Month.APRIL, Year.now());
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }
}