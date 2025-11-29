package com.homs.account_rest_api.accounts.repository;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.mocks.AccountMockFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class AccountJPARepositoryImplTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Account> query;


    @InjectMocks
    AccountJPARepositoryImpl accountRepository;

    private List<Account> accountMockList;
    private AccountMockFactory mockFactory;

    @Before
    public void setUp() {
        mockFactory = new AccountMockFactory();
        accountMockList = List.of(
                mockFactory.mainAccount(),
                mockFactory.checkingAccount()
        );

    }

    @Test
    public void findAll() {
        when(em.createQuery(anyString(), eq(Account.class))).thenReturn(query);
        when(query.setFirstResult(anyInt())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(accountMockList);
        List<Account> result = accountRepository.findAll(10, 3);
        result.forEach(account -> log.info(account.toString()));
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void findAllWithoutParamsShouldReturnUpTo10Results() {
        when(em.createQuery(anyString(), eq(Account.class)))
                .thenReturn(query);
        when(query.setMaxResults(anyInt()))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(accountMockList);
        accountRepository.findAll();

        verify(query).setMaxResults(10);
        verify(query).getResultList();
    }

    @Test
    public void findByIdShouldReturnRequestedAccount() {
        Account mainAccount = mockFactory.mainAccount();
        when(em.find(eq(Account.class), anyString()))
                .thenReturn(mainAccount);
        Optional<Account> result = accountRepository.findById(mainAccount.getAccountId());
        assertTrue(result.isPresent());
        assertEquals(mainAccount.getAccountId(), result.get().getAccountId());
    }

    @Test
    public void saveShouldCreateANewAccount() {
        Account checking = mockFactory.checkingAccount();

        Optional<Account> result = accountRepository.save(checking);

        verify(em).persist(checking);
        assertTrue(result.isPresent());
        assertSame(checking, result.get());
    }

    @Test
    public void deleteByIdShouldDeleteEntity() {
        Account checking = mockFactory.checkingAccount();

        when(em.find(eq(Account.class), anyString()))
                .thenReturn(checking);


        Integer result = accountRepository.deleteById(checking.getAccountId());

        verify(em, times(1)).remove(checking);
        assertEquals(1, result.intValue());
    }

    @Test
    public void deleteByIdShouldNotCallRemoveIfAccountNotExist() {
        when(em.find(eq(Account.class), anyString()))
                .thenReturn(null);

        Integer result = accountRepository.deleteById("invalidId");

        verify(em, never()).remove(any());
        assertEquals(0, result.intValue());
    }

    @Test
    public void updateBalanceShouldReturnEntityToModify() {
        Account mainAccount = mockFactory.mainAccount();

        when(em.find(eq(Account.class), anyString(), any(LockModeType.class)))
                .thenReturn(mainAccount);

        Optional<Account> result = accountRepository.updateBalance(mainAccount);

        verify(em, times(1))
                .find(eq(Account.class), anyString(), any(LockModeType.class));
        assertTrue(result.isPresent());
    }

    @Test
    public void updateBalanceShouldReturnEmptyOptionalWhenAccountDoesNotExist() {
        when(em.find(eq(Account.class), anyString(), any(LockModeType.class)))
                .thenReturn(null);
        Optional<Account> result = accountRepository
                .updateBalance(mockFactory.accountWithoutExpenses());

        verify(em, times(1))
                .find(eq(Account.class), anyString(), any(LockModeType.class));

        assertTrue(result.isEmpty());
    }

    @Test
    public void updateBalanceShouldUpdateOnlyBalance() {
        Account mainAccount = mockFactory.mainAccount();
        BigDecimal newBalance = BigDecimal.valueOf(15_000);
        when(em.find(eq(Account.class), anyString(), any(LockModeType.class)))
                .thenReturn(mainAccount);

        Optional<Account> result = accountRepository
                .updateBalance("acc-main", newBalance);

        assertTrue(result.isPresent());
        assertEquals(newBalance, result.get().getBalance());
    }

    @Test
    public void updateBalanceShouldReturnEmptyWhenNoAccountMatch() {
        when(em.find(eq(Account.class), anyString(), any(LockModeType.class)))
                .thenReturn(null);

        Optional<Account> result = accountRepository
                .updateBalance("acc-main", BigDecimal.valueOf(15_000));

        assertTrue(result.isEmpty());
    }
}