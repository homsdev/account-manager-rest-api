package com.homs.account_rest_api.controller.impl;

import com.homs.account_rest_api.controller.TransactionController;
import com.homs.account_rest_api.dto.CreateTransactionDTO;
import com.homs.account_rest_api.mapper.TransactionMapper;
import com.homs.account_rest_api.model.ApiResponseDTO;
import com.homs.account_rest_api.model.Transaction;
import com.homs.account_rest_api.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/{accountId}/transaction")
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Transaction>> createTransaction(
            @PathVariable String accountId,@Valid @RequestBody CreateTransactionDTO dto) {

        System.out.println("Inputs are valid so executing logic!");
        Transaction toSave = transactionMapper.toEntity(dto);

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
