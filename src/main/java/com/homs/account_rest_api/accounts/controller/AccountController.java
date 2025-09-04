package com.homs.account_rest_api.accounts.controller;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.dto.ApiResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@SuppressWarnings("unused")
@Tag(name = "Account", description = "Account api")
public interface AccountController {

    /**
     * GET All accounts
     *
     * @return Response with all accounts
     */
    @Operation(
            summary = "Get Accounts",
            description = "Fetches all accounts from database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved all accounts",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"data\":[{\"id\":\"644cf9d5-c148-4bb5-bdcb-2c2c9725c200\",\"alias\":\"Savings Account\",\"balance\":120000.00},{\"id\":\"e63e7a68-9e5e-45ab-a833-5dec938f08a8\",\"alias\":\"Main Checking Account\",\"balance\":70000.00}],\"timestamp\":\"2025-08-25T03:10:53.429077076Z\",\"_links\":{\"create\":{\"href\":\"http://localhost/api/accounts\",\"type\":\"POST\"}}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Successful operation but there are no accounts registered",
                    content = @Content(
                            schema = @Schema(hidden = true)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<AccountDTO>>> getAllAccounts();

    /**
     * GET specific account
     *
     * @param id account id
     * @return found account
     */
    @Operation(
            summary = "Fetch an account",
            description = "Fetch the account data by its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account retrieved",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"data\":{\"id\":\"644cf9d5-c148-4bb5-bdcb-2c2c9725c200\",\"alias\":\"Savings Account\",\"balance\":120000.00},\"_links\":{\"self\":{\"href\":\"http://localhost/api/accounts/644cf9d5-c148-4bb5-bdcb-2c2c9725c200\"},\"update\":{\"href\":\"http://localhost/api/accounts/644cf9d5-c148-4bb5-bdcb-2c2c9725c200\",\"type\":\"PATCH\"},\"delete\":{\"href\":\"http://localhost/api/accounts/644cf9d5-c148-4bb5-bdcb-2c2c9725c200\",\"type\":\"DELETE\"},\"all_accounts\":{\"href\":\"http://localhost/api/accounts\"}}}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = "{\"message\":[\"errorMessage\"],\"timestamp\":\"2025-08-25T03:35:22.902263297Z\"}"
                            )
                    )
            ),
    })
    ResponseEntity<ApiResponseDTO<AccountDTO>> getAccountById(@PathVariable String id);

    /**
     * POST Creates new account resource
     *
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
    ResponseEntity<ApiResponseDTO<AccountDTO>> createNewAccount(
            @RequestBody AccountDTO dto
    );

    /**
     * PATCH Updates account balance
     *
     * @param id  account id
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
    ResponseEntity<ApiResponseDTO<AccountDTO>> updateAccountBalance(
            @PathVariable String id, @RequestBody AccountDTO dto
    );

    /**
     * DELETE Deletes account data
     */
    @Operation(
            summary = "Deletes an account",
            description = "Deletes an account by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation", content = @Content(
                    schema = @Schema(hidden = true)
            )),
    })
    ResponseEntity<Void> deleteAccount(@PathVariable String id);

}
