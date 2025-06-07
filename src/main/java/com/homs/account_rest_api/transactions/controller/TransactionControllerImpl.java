package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.model.TransactionMapper;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.Month;
import java.time.Year;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/accounts/{accountId}/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    private Month validateSelectedMonth(String month) {
        try {
            return Month.valueOf(month.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidParametersException("Invalid month value: " + month);
        }
    }

    private Year validateSelectedYear(String year) {
        try {
            return Year.parse(year);
        } catch (DateTimeException ex) {
            throw new InvalidParametersException("Invalid year value: " + year);
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Transaction>> createTransaction(
            @PathVariable String accountId, @RequestBody @Valid CreateTransactionDTO dto) {
        Transaction entity = transactionMapper.toEntity(dto);
        Transaction savedTransaction = transactionService.saveTransaction(entity, accountId);
        ApiResponseDTO<Transaction> response = ApiResponseDTO.<Transaction>builder()
                .data(savedTransaction)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Transaction>>> getTransactionsByMonthAndYear(
            @PathVariable String accountId,
            @RequestParam @NotNull String month,
            @RequestParam @NotNull String year) {

        if (accountId.isBlank()) {
            throw new ResourceNotFoundException(accountId);
        }

        List<Transaction> transactions = transactionService
                .getAllTransactionsByMonthAndYear(
                        validateSelectedMonth(month),
                        validateSelectedYear(year),
                        accountId
                );

        ApiResponseDTO<List<Transaction>> response = ApiResponseDTO.<List<Transaction>>builder()
                .data(transactions)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
