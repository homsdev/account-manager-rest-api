package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<TransactionDto>> createTransaction(
            @PathVariable String accountId, @RequestBody CreateTransactionRequest dto) {
        TransactionDto savedTransactionDto = transactionService.saveTransaction(dto,accountId);
        ApiResponseDTO<TransactionDto> response = ApiResponseDTO.<TransactionDto>builder()
                .data(savedTransactionDto)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TransactionDto>>> getTransactionsByMonthAndYear(
            @PathVariable String accountId,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String year) {
        List<TransactionDto> transactions = transactionService
                .getAllTransactionsByMonthAndYear(
                        month,
                        year,
                        accountId
                );

        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
        }

        ApiResponseDTO<List<TransactionDto>> response = ApiResponseDTO.<List<TransactionDto>>builder()
                .data(transactions)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
