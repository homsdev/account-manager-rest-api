package com.homs.account_rest_api.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
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
@ActiveProfiles("test")
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
    private Category sampleCategory;

    private final String baseEndpoint = "/api/accounts/{accountId}/transactions";

    @Before
    public void setUp() {
        sampleAccount = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .balance(BigDecimal.valueOf(10_000))
                .alias("Sample Account")
                .build();

        sampleCategory = Category.builder()
                .id("sampleId")
                .name("sample")
                .build();

        sampleTransaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(350.75))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.now())
                .alias("Streaming Services")
                .account(sampleAccount)
                .category(sampleCategory)
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


    /**
     * Happy Path - createTransaction should create new resource
     * POST /api/accounts/{accountId}/transactions 201
     *
     * @throws Exception
     */
    @Test
    public void shouldResponseWithHttpCreated() throws Exception {
        when(transactionService.saveTransaction(any(), anyString()))
                .thenReturn(sampleTransaction);
        MvcResult result = mockMvc
                .perform(
                        post(baseEndpoint, sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * POST /api/accounts/{accountId}/transactions 400
     * createTransaction should return array with functional error messages
     *
     * @throws Exception
     */
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
                        post(baseEndpoint, sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidTypeDTO))
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * POST /api/accounts/{accountId}/transactions 400
     * CreateTransaction should return a functional error message if date is on wrong format
     *
     * @throws Exception
     */
    @Test
    public void shouldThrowErrorWhenDateFormatIsIncorrect() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 10_500.10);
        payload.put("type", "INCOME");
        payload.put("date", "2020-13-05");
        payload.put("description", "Test");

        MvcResult result = mockMvc.perform(
                        post(baseEndpoint, sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * POST /api/accounts/{accountId}/transactions 400
     * CreateTransaction should return a functional error message if type is not [INCOME,EXPENSE]
     *
     * @throws Exception
     */
    @Test
    public void shouldThrowErrorWhenEnumIsIncorrect() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 10_500.10);
        payload.put("type", "RECEIPT");
        payload.put("date", "2020-11-05");
        payload.put("description", "Test");

        MvcResult result = mockMvc.perform(
                        post(baseEndpoint, sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(payload))
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * HappyPath 200 - getTransactions should return the list of transactions
     * GET /api/accounts/{accountId}/transactions?month=JUNE&year=2025
     */
    @Test
    public void getTransactionsWithoutParamsShouldReturnCurrentMonthData() throws Exception {

        when(transactionService.getAllTransactionsByMonthAndYear(any(), any(), anyString()))
                .thenReturn(List.of(sampleTransaction, sampleTransaction));

        MvcResult result = mockMvc.perform(
                get(baseEndpoint, "accountId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/{accountId}/transactions?month=JUNE&year=2025 200
     *
     * @throws Exception
     */
    @Test
    public void getTransactionsByMonthAndYearReturnsListOfTransactions() throws Exception {
        when(transactionService.getAllTransactionsByMonthAndYear(any(Month.class), any(Year.class), anyString()))
                .thenReturn(List.of(sampleTransaction));
        String uri = String.format("%s?month=june&year=2025", baseEndpoint);
        MvcResult result = mockMvc.perform(
                        get(uri, sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * Alternative 204 - Should return no content when there are no records that satisfy given parameters
     * GET /api/accounts/{accountId}/transactions?month=JUNE&year=2025 204
     */
    @Test
    public void shouldReturn204WhenNoTransactions() throws Exception {
        when(transactionService.getAllTransactionsByMonthAndYear(any(Month.class), any(Year.class), anyString()))
                .thenReturn(Collections.emptyList());
        String uri = String.format("%s?month=june&year=2025", baseEndpoint);
        MvcResult result = mockMvc.perform(get(uri, "accountId"))
                .andExpect(status().isNoContent())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/{accountId}/transactions 400
     *
     * @throws Exception
     */
    @Test
    public void getTransactionsByMonthAndYearShouldReturn400ForInvalidData() throws Exception {
        MvcResult resultBadMonth = mockMvc.perform(
                        get(String.format("%s?month=mayo&year=2025", baseEndpoint), sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(resultBadMonth.getResponse().getContentAsString());

        MvcResult resultBadYear = mockMvc.perform(
                        get(String.format("%s?month=june&year=NAMCO", baseEndpoint), sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info(resultBadYear.getResponse().getContentAsString());

        MvcResult resultNotAccountId = mockMvc.perform(
                        get(String.format("%s?month=june&year=NAMCO", baseEndpoint), " ")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();
        log.info(resultNotAccountId.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/{accountId}/transactions 404
     * Account does not exists
     *
     * @throws Exception
     */
    @Test
    public void getTransactionsByMonthAndYearShouldThrowExceptionWhenAccountDoesNotExist() throws Exception {
        when(transactionService.getAllTransactionsByMonthAndYear(any(Month.class), any(Year.class), anyString()))
                .thenThrow(new ResourceNotFoundException("Invalid account data"));

        MvcResult result = mockMvc.perform(
                        get("/api/{accountId}/transaction?month=june&year=2025", sampleAccount.getAccountId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * /api/accounts/{accountId}/transactions/load 200
     * Sending a correct file should load all transactions a return a list of created
     */
    @Test
    public void loadTransactionsShouldReturn200WhenOk() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "mock-content".getBytes()
        );

        when(transactionService.loadTransactions(any(MultipartFile.class)))
                .thenReturn(List.of(sampleTransaction, sampleTransaction));

        MvcResult result = mockMvc.perform(
                        multipart(String.format("%s/load", baseEndpoint), "testId")
                                .file(mockFile)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * /api/accounts/{accountId}/transactions/load 400
     * Sending incorrect file format
     */
    @Test
    public void loadTransactionsShouldReturn400WhenBadFormat() throws Exception {
        String mockContent = "This is not a CSV";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                mockContent.getBytes()
        );

        MvcResult result = mockMvc.perform(
                        multipart(String.format("%s/load", baseEndpoint), "test")
                                .file(mockFile)
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * /api/accounts/{accountId}/transactions/load 400
     * Sending empty file or not sending anything
     */
    @Test
    public void loadTransactionsShouldReturn400WhenEmptyFile() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                new byte[0]
        );

        MvcResult resultNotContent = mockMvc.perform(
                        multipart(String.format("%s/load", baseEndpoint), "test")
                                .file(mockFile)
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info(resultNotContent.getResponse().getContentAsString());

        MvcResult resultNoFile = mockMvc.perform(
                        multipart(String.format("%s/load", baseEndpoint), "test")
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info(resultNoFile.getResponse().getContentAsString());
    }
}