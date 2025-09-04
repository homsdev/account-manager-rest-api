package com.homs.account_rest_api.accounts.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@Slf4j
public class AccountControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    private ObjectMapper objectMapper;

    private final String baseUrl = "/api/accounts";
    private final String baseURLWithResource = "/api/accounts/{id}";

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // 200 GET api/accounts
    @Test
    public void getAllAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    // 204 GET api/accounts
    @Test
    @Transactional
    public void getAllAccountsShouldReturnNoContent() throws Exception {
        List<AccountDTO> all = accountService.findAll();
        all.forEach(account -> accountService.deleteById(account.getId()));

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isNoContent())
                .andReturn();
        log.info(result.getResponse().getContentAsString());

    }


    // 200 GET api/accounts/id
    @Test
    public void getAccountByIdShouldReturnFoundAccount() throws Exception {
        MvcResult result = mockMvc.perform(get(baseURLWithResource, "644cf9d5-c148-4bb5-bdcb-2c2c9725c200"))
                .andExpect(status().isOk())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    // 404 GET api/accounts/invalid-id
    @Test
    public void getAccountByIdShouldReturn404WhenAccountNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get(baseURLWithResource, "invalid-id"))
                .andExpect(status().isNotFound())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    // 404 GET api/accounts/blankId
    @Test
    public void getAccountByIdShouldReturn404WhenBlankAccountId() throws Exception {
        MvcResult result = mockMvc.perform(get(baseURLWithResource, "   "))
                .andExpect(status().isNotFound())
                .andReturn();
        log.info(result.getResponse().getContentAsString());

    }

    // 200 GET api/accounts/
    @Test
    public void getAccountByIdShouldRedirectToAllAccountsWhenNullId() throws Exception {
        MvcResult result = mockMvc.perform(get(baseURLWithResource, ""))
                .andExpect(status().isOk())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    // 200 POST api/accounts
    @Test
    public void createNewAccountShouldCreateAnAccount() throws Exception {
        AccountDTO accountToSave = AccountDTO.builder()
                .alias("Test Account")
                .balance(BigDecimal.valueOf(12_500.75))
                .build();

        String requestBody = objectMapper.writeValueAsString(accountToSave);

        MvcResult result = mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andReturn();
        log.info("Request Body: {}", requestBody);
        log.info("Status code: {}", result.getResponse().getStatus());
        log.info(result.getResponse().getContentAsString());
    }


    // 400 POST api/accounts blank alias or negative balance
    @Test
    public void createNewAccountWithBadAliasAndBalanceDTOShouldReturn400() throws Exception {
        AccountDTO dto = AccountDTO.builder()
                .alias("")
                .balance(BigDecimal.valueOf(-1200.00))
                .build();

        String body = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info("Request Body: {}", body);
        log.info("Status code: {}", result.getResponse().getStatus());
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    public void createNewAccountWithNullAliasAndBalanceDTOShouldReturn400() throws Exception {
        AccountDTO dto = AccountDTO.builder()
                .build();

        String body = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(
                        post(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isBadRequest())
                .andReturn();
        log.info("Request Body: {}", body);
        log.info("Status code: {}", result.getResponse().getStatus());
        log.info(result.getResponse().getContentAsString());
    }

    // 200 PATCH api/accounts/id happy path
    @Test
    public void updateAccountBalanceShouldUpdateAccountBalance() throws Exception {
        AccountDTO accountToUpdate = AccountDTO.builder()
                .balance(BigDecimal.valueOf(105_000.75))
                .build();

        String body = objectMapper.writeValueAsString(accountToUpdate);

        MvcResult result = mockMvc.perform(
                        patch(baseURLWithResource, "e63e7a68-9e5e-45ab-a833-5dec938f08a8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isOk())
                .andReturn();

        AccountDTO byId = accountService.findById("e63e7a68-9e5e-45ab-a833-5dec938f08a8");

        log.info("Retrieved account data: {}", byId);
        log.info("Request Body: {}", body);
        log.info(result.getResponse().getContentAsString());
    }

    // 400 PATCH api/accounts/id error with id or balance

    @Test
    public void updateAccountBalanceShouldReturnErrorWhenIdOrBalanceAreMissing() throws Exception {
        AccountDTO accountToUpdateBadReqDTO = AccountDTO.builder()
                .build();

        String body = objectMapper.writeValueAsString(accountToUpdateBadReqDTO);

        MvcResult result = mockMvc.perform(
                        patch(baseURLWithResource, " ")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isBadRequest())
                .andReturn();

        log.info("Request Body: {}", body);
        log.info(result.getResponse().getContentAsString());
    }

    // 404 PATCH api/accounts/id error
    @Test
    public void updateAccountBalanceShouldThrowNotFoundStatusForInvalidId() throws Exception {
        AccountDTO accountToUpdateBadReqDTO = AccountDTO.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        String body = objectMapper.writeValueAsString(accountToUpdateBadReqDTO);

        MvcResult result = mockMvc.perform(
                        patch(baseURLWithResource, "invalid-id")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isNotFound())
                .andReturn();

        log.info("Request Body: {}", body);
        log.info(result.getResponse().getContentAsString());
    }

    // 204 DELETE api/accounts/id with valid id
    @Test
    public void deleteAccountShouldDeleteRequestedAccount() throws Exception {
        mockMvc.perform(
                        delete(baseURLWithResource, "e63e7a68-9e5e-45ab-a833-5dec938f08a8")
                ).andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get(baseURLWithResource, "e63e7a68-9e5e-45ab-a833-5dec938f08a8"))
                .andExpect(status().isNotFound());
    }

    // 204 DELETE api/accounts/id error
    @Test
    public void deleteAccountShouldReturnNoContentAfterSuccessfulDeletion() throws Exception {
        MvcResult result = mockMvc.perform(
                        delete(baseURLWithResource, "id")
                ).andExpect(status().isNoContent())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }
}