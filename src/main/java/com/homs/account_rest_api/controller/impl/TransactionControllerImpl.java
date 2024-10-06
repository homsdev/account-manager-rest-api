package com.homs.account_rest_api.controller.impl;

import com.homs.account_rest_api.controller.TransactionController;
import com.homs.account_rest_api.dto.CreateTransactionDTO;
import com.homs.account_rest_api.mapper.TransactionMapper;
import com.homs.account_rest_api.model.ApiResponseDTO;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    @PostMapping("/{accountId}")
    public ResponseEntity<ApiResponseDTO<Transaction>> createTransaction(
            @PathVariable String accountId, @RequestBody CreateTransactionDTO dto) {

        Transaction toSave = transactionMapper.toEntity(dto);
        System.out.println("Transaction to save");
        System.out.println(toSave);

        Transaction saved = transactionService
                .saveTransaction(toSave, accountId);

        ApiResponseDTO<Transaction> response = ApiResponseDTO.<Transaction>builder()
                .data(Collections.singletonList(saved))
                .message("Operation successful")
                .code(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
