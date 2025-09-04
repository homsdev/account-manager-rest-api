package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.mocks.AccountMockFactory;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import com.homs.account_rest_api.mocks.TransactionMockFactory;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.mapper.TransactionRowMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Month;
import java.time.Year;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionMysqlRepositoryTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private TransactionMysqlRepository transactionRepository;

    private TransactionMockFactory transactionMockFactory;
    private Transaction genericExpense;

    @Before
    public void setUp(){
        transactionMockFactory = new TransactionMockFactory();
        AccountMockFactory accountMockFactory = new AccountMockFactory();
        CategoryMockFactory categoryMockFactory = new CategoryMockFactory();

        genericExpense = transactionMockFactory.createExpense(
                accountMockFactory.mainAccount(),
                categoryMockFactory.transportation(),
                750.60,
                "transportation"
        );
    }

    /**
     * Test to assert when update operation success it returns 1 row affected
     */
    @Test()
    public void saveTransactionShouldReturnTransactionWhenOk() {

        when(jdbcTemplate.update(isNull(), anyMap()))
                .thenReturn(1);

        Optional<Transaction> result = transactionRepository.saveTransaction(genericExpense);

        assertTrue(result.isPresent());
        assertEquals(TransactionType.EXPENSE, result.get().getType());
    }

    /**
     * Test to assert when update operation fails it returns 0 rows affected
     */
    @Test
    public void saveTransactionShouldReturnEmptyWhenFails() {

        when(jdbcTemplate.update(isNull(), anyMap()))
                .thenReturn(0);

        Optional<Transaction> result = transactionRepository.saveTransaction(genericExpense);

        assertTrue(result.isEmpty());
    }

    @Test
    public void saveTransactionShowThrowExceptionWithInvalidTransactionData() {
        Transaction dummy = Transaction.builder()
                .build();
        assertThrows(TransactionInvalidData.class, () ->
                transactionRepository.saveTransaction(dummy));

    }

    @Test
    public void saveTransactionShowThrowExceptionWhenPassedNullValues() {
        assertThrows(TransactionInvalidData.class, () ->
                transactionRepository.saveTransaction(null));
    }

    @Test
    public void getAllTransactionsByMonthShouldReturnRetrievedTransactionFromDB() {

        List<Transaction> expectedTransactions = transactionMockFactory.createCheckingAccountTransactions();

        Map<String, Object> expectedQueryParams = new HashMap<>();
        expectedQueryParams.put("accountId", "sampleId");
        expectedQueryParams.put("year", 2025);
        expectedQueryParams.put("month", 12);

        when(jdbcTemplate.query(isNull(), anyMap(), any(TransactionRowMapper.class)))
                .thenReturn(expectedTransactions);
        List<Transaction> result = transactionRepository
                .getAllTransactionsByMonth("sampleId", Month.DECEMBER, Year.now());
        assertEquals(expectedTransactions.size(), result.size());
        verify(jdbcTemplate).query(isNull(), eq(expectedQueryParams), any(TransactionRowMapper.class));
    }
}