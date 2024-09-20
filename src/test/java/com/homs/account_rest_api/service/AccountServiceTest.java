package com.homs.account_rest_api.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.repository.AccountRepository;
import com.homs.account_rest_api.repository.impl.AccountMysqlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        //
    }

    @Test
    public void name() {
        List<Account> expectedResult = Arrays.asList(new Account(),new Account());

        when(accountRepository.findAll()).thenReturn(expectedResult);

        List<Account> actualResult = accountService.findAll();

        assertEquals(expectedResult,actualResult);
        assertEquals(expectedResult.size(),actualResult.size());
        verify(accountRepository,atMostOnce()).findAll();
    }
}