package com.homs.account_rest_api.transactions.service;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;

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
     * Validates provided date is in the correct format and it's a valid date
     *
     * @param date     date provided by user
     * @param messages List of messages
     */
    private void validateCorrectDateFormat(String date, List<String> messages) {
        if (Objects.isNull(date)) {
            messages.add("Date cannot be null");
        } else {
            try {
                log.info("Validating date: {}, with length {}", date, date.length());
                LocalDate.parse(date.trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch (DateTimeException ex) {
                log.warn(ex.getMessage());
                messages.add("Incorrect date format, correct format is 'dd-MM-yyyy'");
            }
        }
    }

    /**
     * Validates provided transaction type
     *
     * @param transactionType Transaction type provided by the user
     * @param messages        error messages
     */
    private void validateCorrectType(String transactionType, List<String> messages) {
        if (Objects.isNull(transactionType) || transactionType.isBlank()) {
            messages.add("Type cannot be null or blank");
        } else {
            try {
                TransactionType.valueOf(transactionType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                messages.add("Invalid provided transaction type, must be [INCOME,EXPENSE]");
            }
        }
    }

    /**
     * Validates a text provided by the user
     *
     * @param textToValidate text provided by the user
     * @param messages       List of current error messages to append errors found
     */
    private void validateTextField(String textToValidate, String field, List<String> messages) {
        if (Objects.isNull(textToValidate)) {
            messages.add(String.format("%s cannot be null", field));
            return;
        }

        if (textToValidate.isBlank()) {
            messages.add(String.format("%s cannot be empty", field));
        }
    }

    /**
     * Validates user provided amount
     *
     * @param amount   amount to validate
     * @param messages List of error messages to append new issues
     */
    private void validateAmount(BigDecimal amount, List<String> messages) {
        if (Objects.isNull(amount)) {
            messages.add("Amount cannot be null");
        } else {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                messages.add("Amount must be greater than zero");
            }
        }
    }

    /**
     * Validates correct createTransactionRequest
     *
     * @param req {@link CreateTransactionRequest} Request from user to create a new transaction
     * @return {@link List} that contains all found errors in the provided request
     */
    private List<String> validateCreateTransactionRequest(CreateTransactionRequest req) {
        log.info("Executing service validateCreateTransactionRequest");
        List<String> errorMessages = new ArrayList<>();

        if (Objects.isNull(req)) {
            log.warn("Received null request");
            errorMessages.add("Missing transaction information");
            return errorMessages;
        }

        validateAmount(req.getAmount(), errorMessages);
        validateCorrectType(req.getType(), errorMessages);
        validateCorrectDateFormat(req.getDate(), errorMessages);
        validateTextField(req.getAlias(), "Alias", errorMessages);
        validateTextField(req.getCategoryId(), "Category", errorMessages);

        return errorMessages;
    }

    /**
     * Parses given string to valid Month Value
     *
     * @param month string value
     * @return {@link Month}
     * @throws {@link InvalidParametersException} when invalid month string
     */
    private Month parseMonth(String month) {
        if (Objects.isNull(month) || month.isBlank()) {
            return LocalDate.now().getMonth();
        }
        try {
            return Month.valueOf(month.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidParametersException("Invalid provided month");
        }
    }

    /**
     * Parses given string to a valid Year Value
     *
     * @param year string value
     * @return {@link Year}
     * @throws {@link InvalidParametersException} when invalid yar string
     */
    private Year parseYear(String year) {
        if (Objects.isNull(year) || year.isBlank()) {
            return Year.now();
        }
        try {
            if (Integer.parseInt(year) <= 0) {
                throw new InvalidParametersException("Invalid provided year");
            }
            return Year.of(Integer.parseInt(year));
        } catch (NumberFormatException ex) {
            throw new InvalidParametersException("Invalid provided year");
        }
    }

    /**
     * Calculates account balance after a transaction is registered
     *
     * @param currentBalance currentAccountBalance
     * @param transaction    registered transaction
     * @return calculatedBalance
     */
    private BigDecimal calculateBalance(BigDecimal currentBalance, Transaction transaction) {
        TransactionType transactionType = transaction.getType();
        BigDecimal amount = transaction.getAmount();

        return switch (transactionType) {
            case EXPENSE -> currentBalance.subtract(amount);
            case INCOME -> currentBalance.add(amount);
        };
    }

    /**
     * Saves the specified transaction
     * <p>
     * This method saves a new {@link Transaction} associated to an existing account into database
     * If {@link TransactionType} is EXPENSE it subtracts transaction amount from the associated account balance
     * else if is an INCOME ads the transaction amount to the associated account balance
     *
     * @param request   {@link CreateTransactionRequest} object to be saved
     * @param accountId {@link String} Id from the account associated with the transaction
     * @return Saved {@link Transaction} object
     * @throws ResourceNotFoundException   if the associated account does not exist
     * @throws ResourceNotCreatedException if there is an error while persisting the transaction
     */
    @Transactional
    public TransactionDto saveTransaction(CreateTransactionRequest request, String accountId) {
        log.info("Executing service saveTransaction Method");
        if (Objects.isNull(accountId) || accountId.isBlank()) {
            log.warn("Detected invalid accountId");
            throw new InvalidParametersException("Invalid account");
        }

        List<String> messages = validateCreateTransactionRequest(request);

        if (!messages.isEmpty()) {
            throw new InvalidParametersException(messages);
        }

        Account transactionAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(accountId));

        Category category = categoryRepository.getCategory(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(request.getCategoryId()));

        Transaction entity = TransactionMapper.toEntity(request);

        entity.setAccount(transactionAccount);
        entity.setCategory(category);

        log.info("Persisting entity: {}", entity);
        Transaction savedTransaction = transactionRepository.saveTransaction(entity).orElseThrow(
                () -> new ResourceNotCreatedException("")
        );

        log.info("Transaction successfully saved {}", savedTransaction);

        BigDecimal updatedBalance = calculateBalance(transactionAccount.getBalance(), savedTransaction);
        transactionAccount.setBalance(updatedBalance);


        return TransactionMapper.toTransactionDto(savedTransaction);
    }

    /**
     * Retrieves transaction data by the given month and Year
     *
     * @param month     month
     * @param year      year
     * @param accountId accountId
     * @return Transactions list
     */
    public List<TransactionDto> getAllTransactionsByMonthAndYear(String month, String year, String accountId) {
        Month fromMonth = parseMonth(month);
        Year fromYear = parseYear(year);

        accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid account data")
        );


        return transactionRepository.getAllTransactionsByMonth(accountId, fromMonth, fromYear)
                .stream()
                .map(TransactionMapper::toTransactionDto)
                .toList();
    }

}
