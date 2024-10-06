package com.homs.account_rest_api.controller;


import com.homs.account_rest_api.dto.CreateTransactionDTO;
import com.homs.account_rest_api.model.ApiResponseDTO;
import com.homs.account_rest_api.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Transaction", description = "Transaction API")
public interface TransactionController {

    @Operation(
            summary = "Saves a transaction",
            description = "Saves transaction data in datasource"
    )
    public ResponseEntity<ApiResponseDTO<Transaction>> createTransaction(
            @PathVariable String accountId, @RequestBody CreateTransactionDTO dto
    );
}
