package com.homs.account_rest_api.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.dto.UpdateBalance;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.service.AccountService;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(AccountControllerV1Impl.class)
class AccountControllerV1ImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    AccountService accountService;

    private final AccountMockFactory accountMockFactory = new AccountMockFactory();
    private final AccountMapper accountMapper = new AccountMapper();

    @Test
    void getAllAccounts() throws Exception {

        when(accountService.findAll()).thenReturn(
                accountMockFactory.all().stream().map(accountMapper::toDTO).toList()
        );

        MvcResult result = mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();

        verify(accountService, times(1)).findAll();

        String contentAsString = result.getResponse().getContentAsString();

        log.info("Response: {}", contentAsString);
    }

    @Test
    void getAllAccountsShouldReturnNoContentWhenNoAccountsFound() throws Exception {

        when(accountService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isNoContent());

    }

    @Test
    void getAccountById() throws Exception {
        when(accountService.findById(anyLong()))
                .thenReturn(accountMapper.toDTO(accountMockFactory.createMainAccount()));

        MvcResult result = mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.alias").value("Main Account"))
                .andExpect(jsonPath("$.data.balance").value(BigDecimal.valueOf(50_000)))
                .andReturn();

        verify(accountService, times(1)).findById(anyLong());

        log.info("Response: {}", result.getResponse().getContentAsString());
    }

    @Test
    void createAccount() throws Exception {
        CreateAccount account = accountMapper.toCreateAccount(accountMockFactory.createMainAccount());

        String req = objectMapper.writeValueAsString(account);

        when(accountService.saveAccount(any(CreateAccount.class)))
                .thenReturn(accountMapper.toDTO(accountMockFactory.createMainAccount()));

        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        verify(accountService, times(1)).saveAccount(any(CreateAccount.class));

        log.info("Response: {}", result.getResponse().getContentAsString());
    }

    @Test
    void updateBalance() throws Exception {
        UpdateBalance dto = UpdateBalance.builder().balance(BigDecimal.TEN).build();
        AccountDTO updatedBalanceDtoMock = accountMapper.toDTO(accountMockFactory.createMainAccount());
        updatedBalanceDtoMock.setBalance(BigDecimal.TEN);

        String req = objectMapper.writeValueAsString(dto);

        when(accountService.updateBalance(anyLong(), any(UpdateBalance.class)))
                .thenReturn(updatedBalanceDtoMock);

        MvcResult result = mockMvc.perform(patch("/api/v1/accounts/1")
                        .content(req)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(accountService, times(1)).updateBalance(anyLong(), any(UpdateBalance.class));

        log.info("Response: {}", result.getResponse().getContentAsString());
    }

    @Test
    void deleteAccountById() throws Exception {
        when(accountService.deleteById(anyLong())).thenReturn(1);
        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }
}