package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Transaction", description = "Transaction API")
public interface TransactionController {

    @Operation(
            summary = "Saves a transaction",
            description = "Saves transaction data in datasource"
    )
    ResponseEntity<ApiResponseDTO<Transaction>> createTransaction(
            @PathVariable String accountId, @Valid @RequestBody CreateTransactionDTO dto
    );


    @Operation(
            summary = "Retrieves transactions",
            description = "Retrieves transactions in a given month and year"
    )
    ResponseEntity<ApiResponseDTO<List<Transaction>>> getTransactionsByMonthAndYear(
            @PathVariable String accountId,
            @RequestParam String month,
            @RequestParam String year
    );

    @Operation(
            summary = "Load transactions by a csv file",
            description = "Load transactions in a bulk operation by uploading a CSV file"
    )
    ResponseEntity<ApiResponseDTO<List<Transaction>>> loadTransactions(@RequestParam("file") MultipartFile file);
}
