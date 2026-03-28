package com.homs.account_rest_api.mocks;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
public class TransactionMock {

    private static final CategoryMockFactory categoryMockFactory = new CategoryMockFactory();
    private static final AccountMockFactory accountMockFactory = new AccountMockFactory();

    private static Map<String, Category> getCategories() {
        HashMap<String, Category> categories = new HashMap<>();

        categories.put("Games", categoryMockFactory.createGamesCategory());
        categories.put("Food", categoryMockFactory.createFoodCategory());
        categories.put("Transportation", categoryMockFactory.createTransportationCategory());
        categories.put("Salary", categoryMockFactory.createSalaryCategory());

        return categories;
    }

    private static Map<String, Account> getAccounts() {
        HashMap<String, Account> accounts = new HashMap<>();

        accounts.put("Main", accountMockFactory.createMainAccount());
        accounts.put("Checking", accountMockFactory.createCheckingAccount());

        return accounts;
    }

    /**
     * Create a transaction with a specified category and amount
     *
     * @param category Available categories: "Games", "Food", "Transportation", "Salary"
     * @param amount
     * @return
     */
    public static Transaction withCategoryAndAmount(String category, BigDecimal amount) {
        Random random = new Random();
        long transactionId = 1 + random.nextLong(100);
        return Transaction.builder()
                .transactionId(transactionId)
                .alias("Test Transaction")
                .amount(amount)
                .type(TransactionType.EXPENSE)
                .category(getCategories().get(category))
                .account(getAccounts().get("Main"))
                .date(LocalDate.now())
                .build();
    }


}
