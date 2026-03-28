package com.homs.account_rest_api.transactions.service.impl;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.exception.InternalServerError;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.DateFilterTransactionReq;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import com.homs.account_rest_api.transactions.service.BalanceCalculator;
import com.homs.account_rest_api.transactions.service.TransactionService;
import com.homs.account_rest_api.transactions.validations.TransactionRequestValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionRequestValidator requestValidator;
    private final BalanceCalculator balanceCalculator;

    @Override
    @Transactional
    public TransactionDto createTransaction(CreateTransaction dto) {
        log.info("Executing createTransaction");
        log.info("Request: {}", dto);
        requestValidator.validateCreateTransactionRequest(dto);

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(ResourceNotFoundException::new);

        Category category = categoryRepository.getCategory(dto.getCategoryId())
                .orElseThrow(ResourceNotFoundException::new);

        Transaction newTransaction = transactionMapper.toEntity(dto);
        newTransaction.setAccount(account);
        newTransaction.setCategory(category);

        account.setBalance(balanceCalculator.calculate(
                account.getBalance(),
                dto.getAmount(),
                newTransaction.getType()
        ));

        Transaction savedTransaction = transactionRepository.saveTransaction(newTransaction)
                .orElseThrow(InternalServerError::new);

        accountRepository.updateBalance(account)
                .orElseThrow(InternalServerError::new);

        return transactionMapper.toTransactionDto(savedTransaction);
    }

    @Override
    public List<TransactionDto> findTransactionsByAccountAndMonth(DateFilterTransactionReq dto) {
        log.info("Executing findTransactionsByAccountAndMonth");
        log.info("Request: {}", dto);

        requestValidator.validateDateFilterTransactionRequest(dto);

        Month month = Month.of(dto.getMonth());
        Year year = Year.of(dto.getYear());

        List<Transaction> result = transactionRepository
                .getAllTransactionsByMonth(dto.getAccountId(), month, year);

        if (result.isEmpty()) {
            return Collections.emptyList();
        }

        return result.stream().map(transactionMapper::toTransactionDto).toList();
    }
}
