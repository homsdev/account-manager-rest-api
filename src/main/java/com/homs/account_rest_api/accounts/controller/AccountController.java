package com.homs.account_rest_api.accounts.controller;

import com.homs.account_rest_api.accounts.dto.UpdateBalanceDTO;
import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.accounts.model.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@SuppressWarnings("unused")
@Tag(name = "Account", description = "Account api")
public interface AccountController {

    /**
     * GET All accounts
     * @return Response with all accounts
     */
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
    ResponseEntity<ApiResponseDTO<List<Account>>> getAllAccounts();

    /**
     * GET specific account
     * @param id account id
     * @return found account
     */
    @Operation(
            summary = "Fetch an account",
            description = "Fetch the account data by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    ResponseEntity<ApiResponseDTO<Account>> getAccountById(@PathVariable String id);

    /**
     * POST Creates new account resource
     * @param dto new account data
     * @return created account
     */
    @Operation(
            summary = "Creates an account",
            description = "Saves account data in datasource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Resource not created due to functional error")
    })
    ResponseEntity<ApiResponseDTO<Account>> createNewAccount(
            @RequestBody @Valid CreateAccountDto dto
    );

    /**
     * PUT Updates account data
     * @param id account id
     * @param dto new account data
     * @return updated account
     */
    @Operation(
            summary = "Updates account",
            description = "Updates account data for the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Account was not updated due to functional error"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    ResponseEntity<ApiResponseDTO<Account>> updateAccount(
            @PathVariable String id, @RequestBody CreateAccountDto dto);

    /**
     * PATCH Updates account balance
     * @param id account id
     * @param dto new account balance
     * @return modified account
     */
    @Operation(
            summary = "UpdatesAccountBalance",
            description = "Updated selected account balance"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Balance was not updated due to functional error"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    ResponseEntity<ApiResponseDTO<Account>> updateAccountBalance(
            @PathVariable String id, @RequestBody UpdateBalanceDTO dto
    );

    /**
     * DELETE Deletes account data
     */
    @Operation(
            summary = "Deletes an account",
            description = "Deletes an account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    ResponseEntity<Void> deleteAccount(@PathVariable String id);

}
