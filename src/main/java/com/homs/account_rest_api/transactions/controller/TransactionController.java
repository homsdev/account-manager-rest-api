package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Transaction", description = "Transaction API")
public interface TransactionController {

    @Operation(
            summary = "Saves a transaction",
            description = "Saves transaction data in datasource"
    )
    public ResponseEntity<?> createTransaction(
            @PathVariable String accountId,@Valid @RequestBody CreateTransactionDTO dto
    );
}
