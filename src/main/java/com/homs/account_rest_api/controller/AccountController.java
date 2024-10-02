package com.homs.account_rest_api.controller;

import com.homs.account_rest_api.dto.CreateAccountDto;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Account", description = "Account api")
public interface AccountController {

    @Operation(
            summary = "Fetch all accounts",
            description = "Fetches all accounts entities and their data from datasource")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation"),
            @ApiResponse(
                    responseCode = "204",
                    description = "Successful operation but there are no accounts registered",
                    content = @Content
            )
    })
    ResponseEntity<ApiResponseDTO<Account>> getAllAccounts();

    @Operation(
            summary = "Fetch an account",
            description = "Fetch the account data by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
    })
    public ResponseEntity<ApiResponseDTO<Account>> getAccountById(@PathVariable String id);

    @Operation(
            summary = "Creates an account",
            description = "Saves account data in datasource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation")
    })
    public ResponseEntity<ApiResponseDTO<Account>> createNewAccount(
            @RequestBody @Valid CreateAccountDto dto
    );

    @Operation(
            summary = "Updates account balance",
            description = "Updates account balance for the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    public ResponseEntity<ApiResponseDTO<Account>> updateAccountBalance(
            @PathVariable String id, @RequestBody CreateAccountDto dto);

    @Operation(
            summary = "Deletes an account",
            description = "Deletes an account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Succesful Operation",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteAccount(@PathVariable String id);

}
