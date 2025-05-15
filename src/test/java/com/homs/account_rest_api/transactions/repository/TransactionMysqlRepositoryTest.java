package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.model.Account;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Before
    public void setUp(){
        Account sampleAccount = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .alias("sampleAccount")
                .balance(BigDecimal.valueOf(10_000.50))
                .build();
        sampleTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .date(LocalDate.of(2024,10,15))
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

        Optional<Transaction> transaction = transactionRepository.saveTransaction(sampleTransaction);
        assertFalse(transaction.isEmpty());

        Transaction actualResult = transaction.get();

        assertEquals(TransactionType.EXPENSE, actualResult.getType());
    }

    /**
     * Test to assert when update operation fails it returns 0 rows affected
     */
    @Test
    public void saveTransactionShouldReturnEmptyOptionalWhenFails() {
        when(jdbcTemplate.update(anyString(),anyMap()))
                .thenReturn(0);

        Optional<Transaction> transaction = transactionRepository.saveTransaction(sampleTransaction);

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
    public void getAllTransactionsByMonth() {
        //TODO: configure mock call to DB
        //TODO: Assert transactions are received correctly
    }

    public void getAllTransactionsByMonth_shouldReturnEmptyListWithNoSelectedAccount(){
        //TODO: configure mock call to DB
        //TODO: Assert list is empty
    }
}