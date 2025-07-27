package com.homs.account_rest_api.transactions.mapper;

import com.homs.account_rest_api.categories.dto.CategoryDTO;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
public class TransactionMapperTest extends TestCase {

    @Autowired
    private TransactionMapper transactionMapper;

    private CreateTransactionDTO createTransactionDTO;

    @Override
    @Before
    public void setUp() throws Exception {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id("foodId")
                .name("food")
                .build();
        createTransactionDTO = CreateTransactionDTO.builder()
                .amount(BigDecimal.valueOf(10_000))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .description("Gamepass")
                .categoryDTO(categoryDTO)
                .build();
    }

    @Test
    public void shouldMapCreateTransactionDtoToEntity() {
        Transaction entity = transactionMapper.toEntity(createTransactionDTO);

        log.info(entity.toString());

        assertNull(entity.getTransactionId());
        assertNull(entity.getAccount());
        assertEquals("Gamepass", entity.getAlias());
        assertEquals("foodId", entity.getCategory().getId());
        assertEquals("food", entity.getCategory().getName());
    }

    @Test
    public void shouldReturnNullWhenNoCategoryIsPassed() {
        CreateTransactionDTO noCategoryDTO = CreateTransactionDTO.builder()
                .amount(BigDecimal.valueOf(10_000))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .description("Gamepass")
                .categoryDTO(null)
                .build();
        Transaction entity = transactionMapper.toEntity(noCategoryDTO);

        log.info(entity.toString());

        assertNull(entity.getCategory());
    }

    @Test
    public void shouldReturnNullWhenNullValueIsPassed() {
        Transaction entity = transactionMapper.toEntity(null);
        assertNull(entity);
    }
}