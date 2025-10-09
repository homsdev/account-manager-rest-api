package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Transaction", description = "Transaction API")
public interface TransactionController {

    @Operation(
            summary = "Save transaction",
            description = "Persist transaction information",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transaction saved", content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"data\":{\"id\":\"484af010-fe20-4545-8fa2-78649d7a1d41\",\"amount\":12000.0,\"type\":\"EXPENSE\",\"date\":\"2025-05-05\",\"alias\":\"Dummy Expense\",\"accountDTO\":{\"id\":\"e63e7a68-9e5e-45ab-a833-5dec938f08a8\",\"alias\":\"Main Checking Account\",\"balance\":58000.00}},\"timestamp\":\"2025-09-24T17:33:42.542538314Z\"}"
                            )
                    )),
                    @ApiResponse(responseCode = "404", description = "Invalid account", content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"message\":[\"Resource with ID: invalid-account-id not found\"],\"timestamp\":\"2025-09-24T17:46:36.944095652Z\"}"
                            )
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameter")
            }
    )
    ResponseEntity<ApiResponseDTO<TransactionDto>> createTransaction(
            @PathVariable String accountId, @RequestBody CreateTransactionRequest dto
    );


    @Operation(
            summary = "Retrieves transactions",
            description = "Retrieves transactions in a given month and year"
    )
    ResponseEntity<ApiResponseDTO<List<TransactionDto>>> getTransactionsByMonthAndYear(
            @PathVariable String accountId,
            @RequestParam String month,
            @RequestParam String year
    );
}
