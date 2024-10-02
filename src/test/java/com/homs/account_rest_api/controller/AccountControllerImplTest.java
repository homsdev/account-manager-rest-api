package com.homs.account_rest_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AccountControllerImplTest {

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

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
    public void setUp() throws Exception {
        this.mockAccountList.addAll(Arrays.asList(account1, account2, account3));
    }

    @Test
    public void getAllAccountsReturnsData() throws Exception {
        when(accountService.findAll())
                .thenReturn(mockAccountList);
        ResultActions resultActions = mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray());


        verify(accountService, atMostOnce()).findAll();
    }

    @Test
    public void getAllAccountsShouldReturnNoContentWhenThereAreNotAccounts() throws Exception {
        when(accountService.findAll())
                .thenReturn(Collections.<Account>emptyList());

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isNoContent());

        verify(accountService, atMostOnce()).findAll();
    }


    @Test
    public void getAccountById() throws Exception {
        when(accountService.findById("4039d15e-9772-4e3b-abec-ea640a525581"))
                .thenReturn(account2);

        mockMvc.perform(get("/api/accounts/4039d15e-9772-4e3b-abec-ea640a525581"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].alias").value(account2.getAlias()))
                .andExpect(jsonPath("$.data[0].balance").value(account2.getBalance()))
                .andExpect(jsonPath("$.data[0].accountId").value(account2.getAccountId()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()));

        verify(accountService, atMostOnce())
                .findById(eq("4039d15e-9772-4e3b-abec-ea640a525581"));
    }

    @Test
    public void createNewAccount() throws Exception {

        when(accountService.saveAccount(any(Account.class)))
                .thenReturn(account3);

        String reqBody = new ObjectMapper().writeValueAsString(account3);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].accountId").value(account3.getAccountId()))
                .andExpect(jsonPath("$.data[0].alias").value(account3.getAlias()))
                .andExpect(jsonPath("$.data[0].balance").value(account3.getBalance()));

        verify(accountService, atMostOnce()).saveAccount(any(Account.class));
    }

    @Test
    public void updateAccountBalance() throws Exception {
        account3.setBalance(BigDecimal.valueOf(12_350.00));
        when(accountService.updateBalance(any(Account.class)))
                .thenReturn(account3);

        String reqBody = new ObjectMapper().writeValueAsString(account3);

        mockMvc.perform(put("/api/accounts/86e31de1-868b-4444-ad6a-8b4c902e21d2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(reqBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].balance").value(account3.getBalance()));

        verify(accountService, atMostOnce())
                .updateBalance(any(Account.class));
    }

    @Test
    public void deleteAccount() throws Exception {
        when(accountService.deleteById(anyString()))
                .thenReturn(1);

        mockMvc.perform(delete("/api/accounts/86e31de1-868b-4444-ad6a-8b4c902e21d2"))
                .andExpect(status().isNoContent());
    }
}