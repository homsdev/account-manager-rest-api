package com.homs.account_rest_api.summary.controller;

import com.homs.account_rest_api.summary.dto.CategoriesSummaryDto;
import com.homs.account_rest_api.summary.dto.CategorySummaryDto;
import com.homs.account_rest_api.summary.dto.MetadataDto;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import com.homs.account_rest_api.summary.service.SummaryService;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Slf4j
public class SummaryControllerImplTest {

    @MockBean
    SummaryService summaryService;

    @Autowired
    private MockMvc mockMvc;

    /**
     * HttpStatus 200 Happy Path, retrieve month general summary
     * /api/expenses/summary
     */
    @Test
    public void shouldReturnGeneralSummaryWithCode200() throws Exception {
        TransactionDto dummyLargestExpense = TransactionDto.builder()
                .id("123")
                .amount(BigDecimal.valueOf(4500))
                .type(TransactionType.EXPENSE)
                .date(LocalDate.parse("2025-07-10"))
                .alias("Vacations")
                .build();
        MetadataDto metadataDto = MetadataDto.builder()
                .totalExpenses(BigDecimal.valueOf(18_500.60))
                .count(12).build();

        SummaryDto dummyDto = SummaryDto.builder()
                .totalBalance(BigDecimal.valueOf(6_800.60))
                .creditCardExpenses(BigDecimal.valueOf(14_000))
                .savingsGoal(BigDecimal.valueOf(7_00.68))
                .largestExpense(dummyLargestExpense)
                .metadata(metadataDto)
                .build();

        when(summaryService.getSummary())
                .thenReturn(Optional.of(dummyDto));

        MvcResult result = mockMvc.perform(get("/api/expenses/summary"))
                .andExpect(status().isOk())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * HttpStatus 204 alternative, return no content when there is no data for current month
     * /api/expenses/summary
     */
    @Test
    public void shouldReturnNoContentWhenNoDataForCurrentMonth() throws Exception {

        when(summaryService.getSummary())
                .thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get("/api/expenses/summary"))
                .andExpect(status().isNoContent())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * HttpStatus 200 Happy Path
     * /api/expenses/summary/categories
     */
    @Test
    public void shouldReturnSummaryByCategories() throws Exception {

        CategorySummaryDto food = CategorySummaryDto.builder()
                .category("Food")
                .total(BigDecimal.valueOf(3_000))
                .build();

        CategorySummaryDto games = CategorySummaryDto.builder()
                .category("Games")
                .total(BigDecimal.valueOf(1_750.50))
                .build();

        MetadataDto metadataDto = MetadataDto.builder()
                .totalExpenses(BigDecimal.valueOf(4_750.50))
                .count(12).build();

        CategoriesSummaryDto categoriesSummary = CategoriesSummaryDto.builder()
                .categories(List.of(food, games))
                .metadata(metadataDto)
                .build();

        when(summaryService.getCategoriesSummary())
                .thenReturn(Optional.of(categoriesSummary));

        MvcResult result = mockMvc.perform(get("/api/expenses/summary/categories"))
                .andExpect(status().isOk())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * HttpStatus 204 should return no content
     * /api/expenses/summary/categories
     */

    @Test
    public void shouldReturnNoContentWhenNothingToShow() throws Exception {

        when(summaryService.getCategoriesSummary())
                .thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(get("/api/expenses/summary/categories"))
                .andExpect(status().isNoContent())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}