package com.homs.account_rest_api.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Log
public class TransactionControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String baseEndpoint = "/api/accounts/{accountId}/transactions";

    private CreateTransactionRequest validRequest;
    private CreateTransactionRequest invalidRequest;
    private final String VALID_ACCOUNT_ID = "e63e7a68-9e5e-45ab-a833-5dec938f08a8";
    private final String INVALID_ACCOUNT_ID = "invalid-acc-id";
    private final String BLANK_ACCOUNT_ID = " ";
    private final String VALID_CATEGORY_ID = "cat-salary";

    @Before
    public void setUp() {
        validRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(12_000.0))
                .type("EXPENSE")
                .date("05-05-2025")
                .alias("Dummy Expense")
                .categoryId(VALID_CATEGORY_ID)
                .build();
        invalidRequest = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(-12_000.0))
                .type("INVESTMENT")
                .date("05-15-2025")
                .alias("  ")
                .categoryId(VALID_CATEGORY_ID)
                .build();
    }

    /**
     * 200 POST /api/accounts/{accountId}/transactions
     */
    @Test
    public void createTransactionShouldCreateNewTransaction() throws Exception {
        String req = objectMapper.writeValueAsString(validRequest);

        MvcResult result = mockMvc.perform(
                        post(baseEndpoint, VALID_ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(req)
                ).andExpect(status().isCreated())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * 404 POST /api/accounts/{accountId}/transactions
     */
    @Test
    public void createTransactionShouldReturnNotFoundWithInvalidAccountIdOrCategoryId() throws Exception {
        String req = objectMapper.writeValueAsString(validRequest);

        MvcResult invalidAccountRes = mockMvc.perform(
                        post(baseEndpoint, INVALID_ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(req)
                ).andExpect(status().isNotFound())
                .andReturn();

        log.info(invalidAccountRes.getResponse().getContentAsString());

        CreateTransactionRequest invalidCategoryReq = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(12_000.0))
                .type("EXPENSE")
                .date("05-05-2025")
                .alias("Dummy Expense")
                .categoryId("bad-cat-id")
                .build();

        req = objectMapper.writeValueAsString(invalidCategoryReq);

        MvcResult invalidCategoryRes = mockMvc.perform(
                post(baseEndpoint, VALID_ACCOUNT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req)
        ).andReturn();

        log.info(invalidCategoryRes.getResponse().getContentAsString());
    }

    /**
     * 400 POST /api/accounts/{accountId}/transactions
     */
    @Test
    public void createTransactionShouldReturnErrorWhenInvalidParamsInRequest() throws Exception {
        String badReq = objectMapper.writeValueAsString(invalidRequest);

        MvcResult badRequestRes = mockMvc.perform(
                        post(baseEndpoint, VALID_ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badReq)
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info(badRequestRes.getResponse().getContentAsString());
    }

    /**
     * 400 POST /api/accounts/{accountId}/transactions
     */
    @Test
    public void createTransactionShouldReturnErrorWhenBlankAccountId() throws Exception {
        String badReq = objectMapper.writeValueAsString(validRequest);

        MvcResult badRequestRes = mockMvc.perform(
                        post(baseEndpoint, BLANK_ACCOUNT_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badReq)
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info(badRequestRes.getResponse().getContentAsString());
    }

    /**
     * 200 GET /api/accounts/{accountId}/transactions
     */
    @Test
    public void getTransactionsByMonthAndYearShouldReturnTransactions() throws Exception {
        String url = String.format("%s?month=august&year=2025", baseEndpoint);
        MvcResult result = mockMvc.perform(
                        get(url, VALID_ACCOUNT_ID)

                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * 204 GET /api/accounts/{accountId}/transactions
     */
    @Test
    public void getTransactionsByMonthAndYearShouldReturnEmptyTransactions() throws Exception{
        String url = String.format("%s?month=october&year=2025", baseEndpoint);
        MvcResult result = mockMvc.perform(
                        get(url, VALID_ACCOUNT_ID)
                ).andExpect(status().isNoContent())
                .andReturn();
        log.info(result.getResponse().getContentAsString());

    }
}