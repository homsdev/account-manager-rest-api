package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.mapper.TransactionMapper;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
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
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String year) {

        if (accountId.isBlank()) {
            throw new ResourceNotFoundException(accountId);
        }

        if (month == null) {
            month = LocalDate.now().getMonth().name();
        }

        if (year == null) {
            year = String.valueOf(LocalDate.now().getYear());
        }

        List<Transaction> transactions = transactionService
                .getAllTransactionsByMonthAndYear(
                        validateSelectedMonth(month),
                        validateSelectedYear(year),
                        accountId
                );

        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
        }

        ApiResponseDTO<List<Transaction>> response = ApiResponseDTO.<List<Transaction>>builder()
                .data(transactions)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/load")
    @Override
    public ResponseEntity<ApiResponseDTO<List<Transaction>>> loadTransactions(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidParametersException("File not found");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.equals("text/csv")) {
            throw new InvalidParametersException("Invalid file format");
        }

        List<Transaction> loadedTransactions = transactionService.loadTransactions(file);

        ApiResponseDTO<List<Transaction>> response = ApiResponseDTO.<List<Transaction>>builder()
                .data(loadedTransactions)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
