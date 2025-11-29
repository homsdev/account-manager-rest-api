package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import com.homs.account_rest_api.mocks.CategoryMockFactory;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@Slf4j
public class TransactionMapperTest {


    private AccountMockFactory accountMockFactory;
    private CategoryMockFactory categoryMockFactory;

    @Before
    public void setUp() throws Exception {
        accountMockFactory = new AccountMockFactory();
        categoryMockFactory = new CategoryMockFactory();
    }

    @Test
    public void toEntityShouldReturnNullWhenPassedNullValue() {
        Transaction result = TransactionMapper.toEntity(null);
        assertNull(result);
    }

    @Test
    public void toEntityShouldReturnAnTransaction() {
        CreateTransactionRequest dto = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(10_000))
                .type("EXPENSE")
                .date("14-10-2025")
                .alias("Dummy Alias")
                .categoryId("cat-id").build();

        Transaction result = TransactionMapper.toEntity(dto);
        assertNotNull(result);
        log.info(result.toString());
    }

    @Test
    public void toTransactionDtoShouldReturnNullWhenPassedNullValue() {
        TransactionDto result = TransactionMapper.toTransactionDto(null);
        assertNull(result);
    }

    @Test
    public void toTransactionDtoShouldReturnTransactionDTO() {
        Transaction trx = Transaction.builder()
                .transactionId("trx-id")
                .amount(BigDecimal.valueOf(2_000))
                .type(TransactionType.INCOME)
                .date(LocalDate.now())
                .alias("trx-alias")
                .account(accountMockFactory.mainAccount())
                .category(categoryMockFactory.foodCategory()).build();

        TransactionDto result = TransactionMapper.toTransactionDto(trx);
        assertNotNull(result);
        log.info(result.toString());
    }
}