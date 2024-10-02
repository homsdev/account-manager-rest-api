package com.homs.account_rest_api.repository.impl;

import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.mapper.rowmappers.AccountRowMapper;
import com.homs.account_rest_api.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMysqlRepositoryTests {

    private final String VALID_ID = "de2a7490-4c00-492d-bc52-a0c7172eb4ed";
    private Account accountA;
    private Account accountB;
    private List<Account> expectedAccounts;

    @MockBean
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        accountA = Account.builder()
                .accountId("de2a7490-4c00-492d-bc52-a0c7172eb4ed")
                .alias("Dummy Account A")
                .balance(BigDecimal.valueOf(10_000.00))
                .build();
        accountB = Account.builder()
                .accountId("0b3d833e-f31d-48c8-bbfc-101b746eaae7")
                .alias("Dummy Account B")
                .balance(BigDecimal.valueOf(10_500.00))
                .build();
        Account accountC = Account.builder()
                .accountId("0b3d833e-f31d-48c8-bbfc-101b746eedvb9")
                .alias("Dummy Account C")
                .balance(BigDecimal.valueOf(850.75))
                .build();

        expectedAccounts = Arrays.asList(accountA, accountB, accountC);
    }

    /**
     * Test to verify findAll method, it should return a list of all available accounts
     * if query method is successful
     */
    @Test
    public void shouldReturnAllAccounts() {
        when(jdbcTemplate.query(anyString(), any(AccountRowMapper.class)))
                .thenReturn(expectedAccounts);

        List<Account> actualAccounts = accountRepository.findAll();

        assertNotNull(actualAccounts);
        assertEquals(expectedAccounts.size(), actualAccounts.size());
        verify(jdbcTemplate).query(anyString(), any(AccountRowMapper.class));
    }

    /**
     * Test to verify that when a valid id is passed to findById method
     * it should return a valid account
     */
    @Test
    public void shouldReturnAnAccountWhenIDisValid() {
        //Mocks
        List<Account> expectedResult = Collections.singletonList(accountA);

        Map<String, Object> params = new HashMap<>();
        params.put("accountId", VALID_ID);
        //Prepare
        when(jdbcTemplate.query(anyString(), eq(params), any(AccountRowMapper.class)))
                .thenReturn(expectedResult);

        //Act
        Optional<Account> actualResult = accountRepository.findById(VALID_ID);

        //Assertions
        assertTrue("Returns a valid account", actualResult.isPresent());
        assertEquals("Returned account has the correct alias", VALID_ID, actualResult.get().getAccountId());

        verify(jdbcTemplate, atMostOnce()).query(anyString(), eq(params), any(AccountRowMapper.class));
    }

    /**
     * Test to verify that when an invalid id is passed to findById method
     * it will return an empty Optional
     */
    @Test
    public void shouldReturnAnEmptyOptionalWhenIdIsInvalid() {
        List<Account> expectedResult = Collections.emptyList();

        Map<String, Object> params = new HashMap<>();
        String INVALID_ID = "ssdff-sssss--ssss";
        params.put("accountId", INVALID_ID);

        when(jdbcTemplate.query(anyString(), eq(params), any(AccountRowMapper.class)))
                .thenReturn(expectedResult);

        Optional<Account> actualResult = accountRepository.findById(INVALID_ID);

        assertTrue("Result is empty", actualResult.isEmpty());
        verify(jdbcTemplate, times(1))
                .query(anyString(), eq(params), any(AccountRowMapper.class));
    }

    /**
     * Test to verify that when a new account is saved it should return the newly created account
     */
    @Test
    public void shouldReturnTheSameAccountWhenSaveIsSuccessful() {
        Optional<Account> expectedResult = Optional.of(accountB);
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        Optional<Account> actualResult = accountRepository.save(accountB);

        assertTrue("Account is saved", actualResult.isPresent());
        assertEquals("Account is saved correctly", expectedResult.get(), actualResult.get());
        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }

    /**
     * Test to verify that when save method fails it should return an empty optional
     */
    @Test
    public void shouldReturnEmptyOptionalIfSaveFails() {
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        Optional<Account> actualResult = accountRepository.save(accountB);

        assertTrue(actualResult.isEmpty());
        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }

    /**
     * Test to verify that when delete method is called it should return the number of affected rows
     */
    @Test
    public void shouldReturnMoreThan0RowsAffectedWhenDeleting() {
        Integer expectedResult = 1;
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        Integer actualResult = accountRepository.deleteById(VALID_ID);

        assertEquals(expectedResult, actualResult);
        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }

    /**
     * Test to verify that when updated operation is successful it should
     * return the updated account
     */
    @Test
    public void shouldReturnUpdatedAccountWhenUpdatedIsSuccessful() {
        Integer expectedResult = 1;

        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(expectedResult);

        BigDecimal updatedBalance = BigDecimal.valueOf(50_000.00);
        accountB.setBalance(updatedBalance);
        Optional<Account> actualResult = accountRepository.updateBalance(accountB);

        assertTrue(actualResult.isPresent());
        assertEquals(updatedBalance, actualResult.get().getBalance());

        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }

    /**
     * Test to verify that when update operation fails
     * it should return an empty optional
     */
    @Test
    public void shouldReturnEmptyOptionalWhenUpdateFails() {
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(0);

        Optional<Account> actualResult = accountRepository.updateBalance(accountB);

        assertTrue(actualResult.isEmpty());

        verify(jdbcTemplate, atMostOnce()).update(anyString(), anyMap());
    }
}