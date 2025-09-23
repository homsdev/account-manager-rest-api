package com.homs.account_rest_api.transactions.model;

import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransactionMapperTest {
    @Autowired
    private TransactionMapper mapper;

    @Test
    public void shouldCreateTransactionFromDto() {
    }
}