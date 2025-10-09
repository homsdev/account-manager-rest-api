package com.homs.account_rest_api.summary.service;

import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.categories.repository.CategoryRepository;
import com.homs.account_rest_api.summary.dto.CategoriesSummaryDto;
import com.homs.account_rest_api.summary.dto.CategorySummaryDto;
import com.homs.account_rest_api.summary.dto.MetadataDto;
import com.homs.account_rest_api.summary.dto.SummaryDto;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummaryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    private List<Transaction> getAllExpenses(List<Account> accounts, Month month, Year year) {
        return accounts.stream()
                .map(account -> transactionRepository.getAllTransactionsByMonth(
                        account.getAccountId(),
                        month,
                        year
                ))
                .flatMap(List::stream)
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .toList();
    }

    private BigDecimal calculateTotalExpenses(List<Transaction> expenses) {
        return expenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<SummaryDto> getSummary() {
        final LocalDate now = LocalDate.now();
        final Month currentMonth = now.getMonth();
        final Year currentYear = Year.of(now.getYear());

        List<Account> accounts = accountRepository.findAll();
        if (accounts.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Transaction> expenses = getAllExpenses(accounts, currentMonth, currentYear);

        if (expenses.isEmpty()) {
            return Optional.of(SummaryDto.builder()
                    .totalBalance(totalBalance)
                    .creditCardExpenses(BigDecimal.ZERO)
                    .savingsGoal(BigDecimal.ZERO)
                    .largestExpense(null)
                    .metadata(MetadataDto.builder()
                            .totalExpenses(BigDecimal.ZERO)
                            .count(0)
                            .build())
                    .build());
        }

        BigDecimal totalExpenses = calculateTotalExpenses(expenses);

        TransactionDto largestExpense = expenses.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .map(TransactionMapper::toTransactionDto)
                .orElse(null);


        SummaryDto summaryDto = SummaryDto.builder()
                .totalBalance(totalBalance)
                .creditCardExpenses(BigDecimal.ZERO)
                .savingsGoal(BigDecimal.ZERO)
                .largestExpense(largestExpense)
                .metadata(MetadataDto.builder()
                        .count(expenses.size())
                        .totalExpenses(totalExpenses)
                        .build())
                .build();

        return Optional.of(summaryDto);
    }

    public Optional<CategoriesSummaryDto> getCategoriesSummary() {
        final LocalDate now = LocalDate.now();
        final Month currentMonth = now.getMonth();
        final Year currentYear = Year.of(now.getYear());

        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            return Optional.empty();
        }

        List<Transaction> expenses = getAllExpenses(accounts, currentMonth, currentYear);

        if (expenses.isEmpty()) {
            List<CategorySummaryDto> summaryDtos = categoryRepository.getAllCategories().stream()
                    .map(category -> CategorySummaryDto.builder()
                            .category(category.getName())
                            .total(BigDecimal.ZERO)
                            .build()
                    ).toList();
            return Optional.of(CategoriesSummaryDto.builder()
                    .categories(summaryDtos)
                    .metadata(MetadataDto.builder()
                            .count(0)
                            .totalExpenses(BigDecimal.ZERO)
                            .build())
                    .build()
            );
        }

        BigDecimal totalExpenses = calculateTotalExpenses(expenses);

        Map<String, BigDecimal> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        trx->trx.getCategory().getId(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));

        List<CategorySummaryDto> categorySummaries = categoryRepository.getAllCategories().stream()
                .map(category -> CategorySummaryDto.builder()
                        .category(category.getName())
                        .total(categoryTotals.getOrDefault(category.getId(), BigDecimal.ZERO))
                        .build()
                ).toList();

        return Optional.of(
                CategoriesSummaryDto.builder()
                        .categories(categorySummaries)
                        .metadata(MetadataDto.builder()
                                .totalExpenses(totalExpenses)
                                .count(expenses.size())
                                .build())
                        .build()
        );
    }
}
