package com.homs.account_rest_api.transactions.repository;

import com.homs.account_rest_api.AccountRestApiApplication;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountRestApiApplication.class)
@ActiveProfiles("dev")
public class TransactionJPARepositoryImplIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    public void saveTransaction() {
        Optional<Category> salary = categoryRepository.getByName("Salary");
        Optional<Account> account = accountRepository.findById("e63e7a68-9e5e-45ab-a833-5dec938f08a8");
        if (salary.isPresent() && account.isPresent()) {
            Transaction testTransaction = Transaction.builder()
                    .alias("test")
                    .date(LocalDate.now())
                    .amount(BigDecimal.valueOf(5_000.00))
                    .type(TransactionType.EXPENSE)
                    .category(salary.get())
                    .account(account.get()).build();
            Optional<Transaction> result = transactionRepository.saveTransaction(testTransaction);
            assertTrue(result.isPresent());
        }
    }

    @Test
    public void getAllTransactionsByMonth() {

        List<Transaction> result = transactionRepository.getAllTransactionsByMonth(
                "e63e7a68-9e5e-45ab-a833-5dec938f08a8",
                Month.AUGUST,
                Year.now()
        );

        assertEquals(3,result.size());
    }
}