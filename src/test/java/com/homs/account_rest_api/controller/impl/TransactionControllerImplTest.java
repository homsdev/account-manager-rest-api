package com.homs.account_rest_api.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.dto.CreateTransactionDTO;
import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.service.TransactionService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerImplTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    private final Account mockAccount = Account.builder()
            .accountId(UUID.randomUUID().toString())
            .alias("MockAccount")
            .balance(BigDecimal.valueOf(10_000.50))
            .build();

    private final Transaction mockTransactionA = Transaction.builder()
            .transactionId(UUID.randomUUID().toString())
            .date(LocalDate.of(2024, 10, 15))
            .type(TransactionType.EXPENSE)
            .amount(BigDecimal.valueOf(750.99))
            .account(mockAccount)
            .alias("DISNEY+")
            .build();

    @Test
    public void createTransactionShouldEndOk() throws Exception {
        when(transactionService.saveTransaction(any(Transaction.class), anyString()))
                .thenReturn(mockTransactionA);

        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setDescription(mockTransactionA.getAlias());
        dto.setTransactionType(mockTransactionA.getType().getValue());
        dto.setDate(LocalDate.now().toString());
        dto.setAmount(BigDecimal.valueOf(5_000.00));

        String reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.data[0].amount").value(mockTransactionA.getAmount()));
    }

    @Test
    public void postTransactionEndsBAD() throws Exception {
        when(transactionService.saveTransaction(any(Transaction.class), anyString()))
                .thenThrow(new ResourceNotCreatedException("Error"));

        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setDescription(mockTransactionA.getAlias());
        dto.setTransactionType(mockTransactionA.getType().getValue());
        dto.setDate(LocalDate.now().toString());
        dto.setAmount(BigDecimal.valueOf(5_000.00));

        String reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));
    }

    @Test
    public void postTransactionShouldFailIfDateIsNotValid() throws Exception {
        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setDescription(mockTransactionA.getAlias());
        dto.setTransactionType(mockTransactionA.getType().getValue());
        dto.setDate("2023-02-29");
        dto.setAmount(BigDecimal.valueOf(5_000.00));

        String reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));

        dto.setDate("02-14-1999");
        reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));
    }

    @Test
    public void postTransactionShouldFailIfTypeIsIncorrect() throws Exception {
        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setDescription(mockTransactionA.getAlias());
        dto.setTransactionType("EXP");
        dto.setDate(LocalDate.now().toString());
        dto.setAmount(BigDecimal.valueOf(5_000.00));

        String reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));


        dto.setTransactionType("INC");
        reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(0)));


    }

    @Test
    public void postTransactionShouldEndOkWithDifferentTransactionTypeInputs() throws Exception {
        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setDescription(mockTransactionA.getAlias());
        dto.setTransactionType("expense");
        dto.setDate(LocalDate.now().toString());
        dto.setAmount(BigDecimal.valueOf(5_000.00));

        String reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));


        dto.setTransactionType("income");
        reqBody = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/" + mockAccount.getAccountId() + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));
    }
}