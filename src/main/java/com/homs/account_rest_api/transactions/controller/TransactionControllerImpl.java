package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponse;
import com.homs.account_rest_api.transactions.model.Transaction;
import com.homs.account_rest_api.transactions.model.TransactionMapper;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collections;

@RestController
@Validated
@RequestMapping("/api/{accountId}/transaction")
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    @PostMapping
    public ResponseEntity<?> createTransaction(
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
}
