package com.homs.account_rest_api.transactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.mocks.TransactionMock;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(TransactionControllerV1Impl.class)
class TransactionControllerV1ImplTest {

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "/api/v1/accounts";

    private final TransactionMapper transactionMapper = new TransactionMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("GET /api/v1/accounts/{accountId}/transactions")
    class GetTransactions {

        @Test
        @DisplayName("Should return 200 list of transactions by month, year and accountId")
        void allByMonthYearAndAccountId() throws Exception {
            Transaction food = TransactionMock
                    .withCategoryAndAmount("Food", BigDecimal.valueOf(15_000));
            Transaction transportation = TransactionMock
                    .withCategoryAndAmount("Transportation", BigDecimal.valueOf(10_000));


            when(transactionService.findTransactionsByAccountAndMonth(any(DateFilterTransactionReq.class)))
                    .thenReturn(Stream.of(food, transportation).map(transactionMapper::toTransactionDto).toList());

            MvcResult result = mockMvc.perform(get(baseUrl + "/1/transactions")
                            .param("month", "1")
                            .param("year", "2023")
                            .param("accountId", "123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andReturn();

            log.info("GetAllByMonthYearAndAccountId Response: {}", result.getResponse().getContentAsString());
        }

        @Test
        @DisplayName("Should return 204 No Content when no transactions found")
        void allByMonthYearAndAccountIdShouldHandleNotFoundTransactions() throws Exception {
            when(transactionService.findTransactionsByAccountAndMonth(any(DateFilterTransactionReq.class)))
                    .thenReturn(List.of());

            MvcResult result = mockMvc.perform(get(baseUrl + "/1/transactions")
                            .param("month", "1")
                            .param("year", "2023")
                            .param("accountId", "123"))
                    .andExpect(status().isNoContent())
                    .andReturn();

            log.info("GetAllByMonthYearAndAccountId Response: {}", result.getResponse().getContentAsString());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when invalid parameters")
        void allByMonthYearAndAccountIdShouldHandleBadRequest() throws Exception {
            List<String> errors = List.of("Year cannot be null","Month cannot be null", "Account id cannot be null");

            when(transactionService.findTransactionsByAccountAndMonth(any(DateFilterTransactionReq.class)))
                    .thenThrow(new InvalidParametersException(errors));

            MvcResult result = mockMvc.perform(get(baseUrl + "/1/transactions"))
                    .andExpect(status().isBadRequest()).andReturn();

            log.info("GetAllByMonthYearAndAccountId Response: {}", result.getResponse().getContentAsString());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/accounts/{accountId}/transactions")
    class SaveTransaction {

        @Test
        @DisplayName("Should return 200 when transaction is created successfully")
        void createTransactionSuccessP() throws Exception {
            CreateTransaction req = CreateTransaction.builder()
                    .amount(BigDecimal.valueOf(50_000))
                    .type("EXPENSE")
                    .date("05-03-2026")
                    .alias("Test")
                    .categoryId(1L)
                    .accountId(1L).build();

            TransactionDto resultDto = TransactionDto.builder()
                    .id(1L)
                    .amount(BigDecimal.valueOf(50_000))
                    .type(TransactionType.EXPENSE)
                    .date(LocalDate.parse("05-03-2026", DateTimeFormatter.ofPattern("dd-MM-uuuu")))
                    .description("Test")
                    .build();

            when(transactionService.createTransaction(any(CreateTransaction.class)))
                    .thenReturn(resultDto);

            MvcResult result = mockMvc.perform(
                            post(baseUrl + "/1/transactions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            log.info("CreateTransaction Response: {}", result.getResponse().getContentAsString());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when invalid or missing parameters")
        void createTransactionWithMissingParamsError() throws Exception {
            CreateTransaction req = CreateTransaction.builder().build();

            List<String> errors = List.of("Date cannot be null", "Alias cannot be null", "Amount cannot be null", "Type cannot be null");
            when(transactionService.createTransaction(any(CreateTransaction.class)))
                    .thenThrow(new InvalidParametersException(errors));

            MvcResult result = mockMvc.perform(
                            post(baseUrl + "/1/transactions")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))
                    )
                    .andExpect(status().isBadRequest())
                    .andReturn();

            log.info("CreateTransaction Response: {}", result.getResponse().getContentAsString());
        }


    }

}