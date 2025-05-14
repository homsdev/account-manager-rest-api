package com.homs.account_rest_api.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.model.TransactionMapper;
import com.homs.account_rest_api.transactions.service.TransactionService;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("Test")
@Log
public class TransactionControllerImplTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionMapper transactionMapper;

    private ObjectMapper objectMapper;

    private Transaction sampleTransaction;
    private Account sampleAccount;
    private CreateTransactionDTO sampleDTO;

    @Before
    public void setUp() throws Exception {
        sampleAccount = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .balance(BigDecimal.valueOf(10_000))
                .alias("Sample Account")
                .build();
        sampleTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(350.75))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .alias("Streaming Services")
                .account(sampleAccount)
                .build();

        sampleDTO = CreateTransactionDTO.builder()
                .amount(BigDecimal.valueOf(350.75))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .description("Streaming Services")
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    public void shouldResponseWithHttpCreated() throws Exception {
        when(transactionService.saveTransaction(any(), anyString()))
                .thenReturn(sampleTransaction);
        MvcResult result = mockMvc
                .perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void shouldThrowErrorWithAnyNullField() throws Exception {
        CreateTransactionDTO invalidTypeDTO = CreateTransactionDTO
                .builder()
                .type(null)
                .amount(null)
                .date(null)
                .description(null)
                .build();

        MvcResult result = mockMvc
                .perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidTypeDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void shouldThrowErrorWhenDateFormatIsIncorrect() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 10_500.10);
        payload.put("type", "INCOME");
        payload.put("date", "2020-13-05");
        payload.put("description", "Test");

        MvcResult result = mockMvc.perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void shouldThrowErrorWhenEnumIsIncorrect() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 10_500.10);
        payload.put("type", "RECEIPT");
        payload.put("date", "2020-11-05");
        payload.put("description", "Test");

        MvcResult result = mockMvc.perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void whenDescriptionIsInvalid_shouldReturnBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 10_500.10);
        payload.put("type", "INCOME");
        payload.put("date", "2020-11-05");
        payload.put("description", "");

        MvcResult result = mockMvc.perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void whenMissingFields_shouldReturnBadRequest() throws Exception{
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "INCOME");
        payload.put("description", "");

        MvcResult result = mockMvc.perform(
                        post("/api/{accountId}/transaction", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }
}