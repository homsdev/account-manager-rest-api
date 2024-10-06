package com.homs.account_rest_api.repository.impl;

import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionMysqlRepositoryTest {

    private final Account mockAccount = Account.builder()
            .accountId(UUID.randomUUID().toString())
            .alias("MockAccount")
            .balance(BigDecimal.valueOf(10_000.50))
            .build();

    private final Transaction mockTransactionA = Transaction.builder()
            .transactionId(UUID.randomUUID().toString())
            .date(LocalDate.of(2024,10,15))
            .type(TransactionType.EXPENSE)
            .amount(BigDecimal.valueOf(750.99))
            .account(mockAccount)
            .alias("DISNEY+")
            .build();

    @MockBean
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void shouldReturnOptionalWithTransactionWhenOk() {
        when(jdbcTemplate.update(anyString(), anyMap()))
                .thenReturn(1);

        Optional<Transaction> transaction = transactionRepository.saveTransaction(mockTransactionA);
        assertFalse(transaction.isEmpty());

        Transaction actualResult = transaction.get();

        assertEquals(TransactionType.EXPENSE, actualResult.getType());
    }

    @Test
    public void saveTransactionShouldReturnEmptyOptionalWhenFails() {
        when(jdbcTemplate.update(anyString(),anyMap()))
                .thenReturn(0);

        Optional<Transaction> transaction = transactionRepository.saveTransaction(mockTransactionA);

        assertTrue(transaction.isEmpty());
    }
}