package com.homs.account_rest_api.transactions.controller;

import com.homs.account_rest_api.dto.ErrorApiResponse;
import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.TransactionApiResponse;
import com.homs.account_rest_api.transactions.dto.TransactionListApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Transaction", description = "Transaction API")
public interface TransactionController {

    @Operation(
            summary = "Save transaction",
            description = "Persist transaction information",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Transaction saved",
                            content = @Content(
                                    schema = @Schema(implementation = TransactionApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Invalid account",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameter",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorApiResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<TransactionApiResponse> createTransaction(
            @PathVariable Long accountId, @RequestBody CreateTransaction dto
    );


    @Operation(
            summary = "Retrieves transactions",
            description = "Retrieves transactions in a given month and year"
    )
    ResponseEntity<TransactionListApiResponse> getTransactionsByMonthAndYear(
            @PathVariable Long accountId,
            @RequestParam Integer month,
            @RequestParam Integer year
    );
}
