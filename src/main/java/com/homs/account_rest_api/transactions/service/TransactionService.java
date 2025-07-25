package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.transactions.exceptions.TransactionInvalidData;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

/**
 * Transaction service class provides methods to perform CRUD
 * operations over transactions
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    private static final Category DEFAULT_CATEGORY = Category.builder()
            .id("eed64a18-20d2-4644-84d6-6d5dd7732db8")
            .name("Others")
            .build();

    /**
     * Saves the specified transaction
     * <p>
     * This method saves a new {@link Transaction} associated to an existing account into database
     * If {@link TransactionType} is EXPENSE it subtracts transaction amount from the associated account balance
     * else if is an INCOME ads the transaction amount to the associated account balance
     *
     * @param transaction {@link Transaction} object to be saved
     * @param accountId   {@link String} Id from the account associated with the transaction
     * @return Saved {@link Transaction} object
     * @throws ResourceNotFoundException   if the associated account does not exist
     * @throws ResourceNotCreatedException if there is an error while persisting the transaction
     */
    @Transactional
    public Transaction saveTransaction(Transaction transaction, final String accountId) {

        if (Objects.isNull(accountId)) {
            throw new TransactionInvalidData("Missing account info");
        }

        if (Objects.isNull(transaction)) {
            throw new TransactionInvalidData("Missing transaction info");
        }

        String categoryId = Optional.ofNullable(transaction.getCategory())
                .map(Category::getId)
                .orElse(null);

        Category category = Optional.ofNullable(categoryId)
                .flatMap(categoryRepository::getCategory)
                .orElse(DEFAULT_CATEGORY);

        transaction.setCategory(category);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Associated account does not exist"));

        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAccount(account);

        BigDecimal actualBalance = account.getBalance();
        BigDecimal updatedBalance = null;

        if (transaction.getType() == TransactionType.INCOME) {
            updatedBalance = actualBalance.add(transaction.getAmount());
        }

        if (transaction.getType() == TransactionType.EXPENSE) {
            updatedBalance = actualBalance.subtract(transaction.getAmount());
        }

        account.setBalance(updatedBalance);

        accountRepository.updateBalance(account)
                .orElseThrow(() -> new ResourceNotFoundException("Error while updating transaction balance"));

        return transactionRepository.saveTransaction(transaction)
                .orElseThrow(() -> new ResourceNotCreatedException("Error while saving transaction"));
    }

    /**
     * Retrieves transaction data by the given month and Year
     *
     * @param month     month
     * @param year      year
     * @param accountId accountId
     * @return Transactions list
     */
    public List<Transaction> getAllTransactionsByMonthAndYear(Month month, Year year, String accountId) {

        accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid account data")
        );

        return transactionRepository.getAllTransactionsByMonth(accountId, month, year);
    }

    /**
     * Loads transactions into storage by reading info from a CSV file
     *
     * @param file
     * @return
     */
    public List<Transaction> loadTransactions(MultipartFile file) {
        List<Transaction> trxs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            if ((line = reader.readLine()) != null) {
                log.info("Skipping headers: {}", line);
            } else {
                throw new InvalidParametersException("Something went wrong with shared file");
            }

            Map<String, Category> availableCategories = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                log.info("data line: {}", line);
                if (line.trim().isEmpty()) continue;
                String[] values = line.split(",");
                String categoryName = values[5];

                if (!availableCategories.containsKey(categoryName)) {
                    Category currentCategory = categoryRepository.getByName(categoryName)
                                    .orElse(DEFAULT_CATEGORY);
                    availableCategories.put(currentCategory.getName(), currentCategory);
                }

                Transaction newTransaction = Transaction.builder()
                        .transactionId(UUID.randomUUID().toString())
                        .amount(new BigDecimal(values[0]))
                        .type(TransactionType.valueOf(values[1]))
                        .date(LocalDate.parse(values[2]))
                        .account(Account.builder().accountId(values[3]).build())
                        .alias(values[4])
                        .category(availableCategories.get(categoryName))
                        .build();

                log.info("Created transaction: {}", newTransaction.toString());
                Optional<Transaction> createdTrx = transactionRepository.saveTransaction(newTransaction);
                createdTrx.ifPresent(trxs::add);
            }
        } catch (IOException ex) {
            throw new InvalidParametersException("Something went wrong with shared file");
        }
        return trxs;
    }

}
