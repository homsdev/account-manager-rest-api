package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.transactions.dto.*;
import com.homs.account_rest_api.transactions.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class TransactionControllerV1Impl implements TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionApiResponse> createTransaction(
            @PathVariable Long accountId,
            @RequestBody CreateTransaction dto) {
        log.info("Executing createTransaction");

        dto.setAccountId(accountId);

        TransactionDto transaction = transactionService.createTransaction(dto);

        TransactionApiResponse response = TransactionApiResponse.builder()
                .data(transaction)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/transactions")
    @Override
    public ResponseEntity<TransactionListApiResponse> getTransactionsByMonthAndYear(
            @PathVariable Long accountId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        DateFilterTransactionReq filterParams = DateFilterTransactionReq.builder()
                .accountId(accountId)
                .month(month)
                .year(year)
                .build();

        List<TransactionDto> result = transactionService.findTransactionsByAccountAndMonth(filterParams);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        TransactionListApiResponse response = TransactionListApiResponse.builder()
                .data(result)
                .total(result.size())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
