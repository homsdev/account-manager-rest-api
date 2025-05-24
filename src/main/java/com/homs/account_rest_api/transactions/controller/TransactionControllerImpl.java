package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponse;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.model.TransactionMapper;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/{accountId}/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    private Month validateSelectedMonth(String month){
        try {
            return Month.valueOf(month.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidParametersException("Invalid month value: " + month);
        }
    }

    private Year validateSelectedYear(String year){
        try {
            return Year.parse(year);
        } catch (DateTimeException ex) {
            throw new InvalidParametersException("Invalid year value: " + year);
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(
            @PathVariable String accountId, @RequestBody @Valid CreateTransactionDTO dto) {
        Transaction entity = transactionMapper.toEntity(dto);
        Transaction savedTransaction = transactionService.saveTransaction(entity, accountId);
        ApiResponse<Transaction> response = ApiResponse.<Transaction>builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.toString())
                .data(Collections.singletonList(savedTransaction))
                .timestamp(Instant.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<Transaction>> getTransactionsByMonthAndYear(
            @PathVariable @NotBlank String accountId,
            @RequestParam @NotNull String month,
            @RequestParam @NotNull String year) {
        List<Transaction> transactions = transactionService
                .getAllTransactionsByMonthAndYear(
                        validateSelectedMonth(month),
                        validateSelectedYear(year),
                        accountId
                );

        ApiResponse<Transaction> response = ApiResponse.<Transaction>builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(transactions)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
