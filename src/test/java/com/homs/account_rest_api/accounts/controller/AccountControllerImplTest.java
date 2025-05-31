package com.homs.account_rest_api.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.accounts.dto.UpdateBalanceDTO;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.service.AccountService;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("Test")
@Slf4j
public class AccountControllerImplTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/accounts";
    private final String baseResourceUrl = "/api/accounts/{id}";

    private final List<Account> mockAccountList = new ArrayList<>();

    private final Account account1 = Account
            .builder()
            .accountId("9bcfb31a-7e24-4413-97b3-da0a3a389fa6")
            .alias("Fake account A")
            .balance(BigDecimal.valueOf(10_000))
            .build();
    private final Account account2 = Account
            .builder()
            .accountId("4039d15e-9772-4e3b-abec-ea640a525581")
            .alias("Fake account B")
            .balance(BigDecimal.valueOf(75_000))
            .build();

    private final Account account3 = Account
            .builder()
            .accountId("86e31de1-868b-4444-ad6a-8b4c902e21d2")
            .alias("Fake account C")
            .balance(BigDecimal.valueOf(50_000))
            .build();

    @Before
    public void setUp() {
        this.mockAccountList.addAll(Arrays.asList(account1, account2, account3));
        this.objectMapper = new ObjectMapper();
    }

    /**
     * GET /api/accounts 200
     * GetAllAccounts should return 204 when request is ok but there are no resources to show
     *
     * @throws Exception
     */
    @Test
    public void getAllAccountsShouldReturn204_WhenEmptyResponse() throws Exception {
        when(accountService.findAll())
                .thenReturn(Collections.emptyList());
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isNoContent())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts 200
     * GetAllAccounts should return 200 and a list of available accounts
     *
     * @throws Exception
     */
    @Test
    public void getAllAccountsShouldReturn200_WhenResponseWithFullList() throws Exception {
        when(accountService.findAll())
                .thenReturn(mockAccountList);

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/{accountId} 404
     * Get Account by id should return 404 when could not find the resource
     *
     * @throws Exception
     */
    @Test
    public void getAccountByIdShouldReturn404_whenAccountNotExists() throws Exception {
        when(accountService.findById(anyString()))
                .thenThrow(new ResourceNotFoundException());
        MvcResult result = mockMvc.perform(get(baseResourceUrl, "testAccountId"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/testAccountId 200
     * Get Account should return 200 and a single resource when searched resource exists
     *
     * @throws Exception
     */
    @Test
    public void getAccountByIdShouldReturn200_whenAccountExists() throws Exception {
        when(accountService.findById(anyString()))
                .thenReturn(account1);

        MvcResult result = mockMvc.perform(get(baseResourceUrl, "testAccountId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isMap())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * GET /api/accounts/%20 404
     * Get Account should return 404 when accountId is missing or with whitespace characters
     *
     * @throws Exception
     */
    @Test
    public void getAccountByIdShouldReturn400_whenMissingParameters() throws Exception {
        MvcResult result = mockMvc.perform(get(baseResourceUrl, " "))
                .andExpect(status().isNotFound())
                .andReturn();
        log.info(result.getRequest().getRequestURI());
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * /POST /api/accounts 201
     * Create new account should return 201 and the resource when valid data is given
     *
     * @throws Exception
     */
    @Test
    public void createNewAccount_ShouldReturn201() throws Exception {
        CreateAccountDto dto = CreateAccountDto.builder()
                .accountAlias("Test A")
                .accountBalance(BigDecimal.valueOf(35000))
                .build();

        when(accountService.saveAccount(any(Account.class)))
                .thenReturn(account1);

        MvcResult result = mockMvc.perform(
                        post(baseUrl)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }


    /**
     * /POST /api/accounts 400
     * When Alias is missing or balance is minor than 0 it should return 400
     *
     * @throws Exception
     */
    @Test
    public void createNewAccount_ShouldReturn400() throws Exception {
        CreateAccountDto badBalanceDTO = CreateAccountDto.builder()
                .accountAlias(" ")
                .accountBalance(BigDecimal.valueOf(-13000))
                .build();

        MvcResult result = mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(badBalanceDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isArray())
                .andExpect(jsonPath("$.message.length()").value(2))
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }


    /**
     * PATCH /api/accounts/{id} 200
     * @throws Exception
     */
    @Test
    public void updateAccountBalance_shouldReturn200() throws Exception{
        UpdateBalanceDTO updateBalanceDTO = UpdateBalanceDTO.builder()
                .updatedBalance(BigDecimal.valueOf(10_000))
                .build();
        MvcResult result = mockMvc.perform(
                patch(baseResourceUrl, "testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBalanceDTO))
        ).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * PATCH /api/accounts/{id} 400
     * @throws Exception
     */
    @Test
    public void updateAccountBalance_shouldReturn400() throws Exception{
        UpdateBalanceDTO updateBalanceDTO = UpdateBalanceDTO.builder()
                .updatedBalance(BigDecimal.valueOf(-10_000))
                .build();
        MvcResult result = mockMvc.perform(
                patch(baseResourceUrl, "testId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBalanceDTO))
        ).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    /**
     * PATCH /api/accounts/{id} 404
     * @throws Exception
     */
    @Test
    public void updateAccountBalance_shouldReturn404() throws Exception{
        UpdateBalanceDTO updateBalanceDTO = UpdateBalanceDTO.builder()
                .updatedBalance(BigDecimal.valueOf(10_000))
                .build();
        MvcResult result = mockMvc.perform(
                patch(baseResourceUrl, " ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBalanceDTO))
        ).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

}