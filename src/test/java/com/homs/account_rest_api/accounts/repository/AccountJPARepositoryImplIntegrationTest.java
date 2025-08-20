package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.AccountRestApiApplication;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = AccountRestApiApplication.class)
@ActiveProfiles("dev")
@Slf4j
public class AccountJPARepositoryImplIntegrationTest {

    @Autowired
    AccountRepository accountRepository;

    private AccountMockFactory mockFactory;

    @Before
    public void setUp() {
        mockFactory = new AccountMockFactory();
    }

    @Test
    public void findAll() {
        List<Account> result = accountRepository.findAll();
        log.info(result.toString());
        assertEquals(2, result.size());
    }

    @Test
    public void testFindAll() {
        List<Account> result = accountRepository.findAll(1, 2);
        log.info(result.toString());
        assertEquals(1, result.size());
    }

    @Test
    public void findById() {
        Account savingsAccount = Account.builder()
                .accountId("644cf9d5-c148-4bb5-bdcb-2c2c9725c200")
                .balance(BigDecimal.valueOf(120_000))
                .alias("Savings Account")
                .build();
        Optional<Account> result = accountRepository.findById("644cf9d5-c148-4bb5-bdcb-2c2c9725c200");
        assertTrue(result.isPresent());
        assertEquals(savingsAccount.getAccountId(), result.get().getAccountId());
        assertEquals(0, savingsAccount.getBalance().compareTo(result.get().getBalance()));
        assertEquals(savingsAccount.getAlias(), result.get().getAlias());
    }

    @Test
    @Transactional
    public void save() {
        Account account = mockFactory.mainAccount();
        account.setAccountId(null);
        Optional<Account> result = accountRepository.save(account);
        assertTrue(result.isPresent());
        assertEquals(0, account.getBalance().compareTo(result.get().getBalance()));
        assertEquals(account.getAlias(), result.get().getAlias());
    }

    @Test
    @Transactional
    public void deleteById() {
        Integer result = accountRepository.deleteById("644cf9d5-c148-4bb5-bdcb-2c2c9725c200");
        assertEquals(1, result.intValue());
    }

    @Test
    @Transactional
    public void updateBalance() throws ResourceNotFoundException {
        Account account = accountRepository.findById("e63e7a68-9e5e-45ab-a833-5dec938f08a8")
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        Account accountToUpdate = accountRepository.updateBalance(account)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for update"));
        log.info("Setting balance to ZERO");
        accountToUpdate.setBalance(BigDecimal.ZERO);
        log.info("Retrieving account again");
        Account updatedAccount = accountRepository
                .findById("e63e7a68-9e5e-45ab-a833-5dec938f08a8").orElse(mockFactory.checkingAccount());
        log.info("Updated Account: {}", updatedAccount.toString());
        assertEquals(0,
                updatedAccount.getBalance().compareTo(BigDecimal.ZERO));

    }
}