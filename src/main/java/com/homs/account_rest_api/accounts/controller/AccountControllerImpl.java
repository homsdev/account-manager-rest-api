package com.homs.account_rest_api.accounts.controller;


import com.homs.account_rest_api.accounts.dto.UpdateBalanceDTO;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.service.AccountService;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDTO<List<Account>>> getAllAccounts() {
        List<Account> all = accountService.findAll();

        if (all.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponseDTO<List<Account>> response =
                ApiResponseDTO.<List<Account>>builder()
                        .data(all)
                        .timestamp(Instant.now())
                        .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<Account>> getAccountById(@PathVariable("id") String id) {
        log.info("Executing find by id for: {}", id);
        if (id.isBlank()) {
            throw new ResourceNotFoundException(String.format("Account not found for ID: %s", id));
        }
        Account account = accountService.findById(id);
        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(account)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseDTO<Account>> createNewAccount(@Valid @RequestBody CreateAccountDto dto) {
        log.info("Executing createNewAccount");
        Account newAccount = accountMapper.toEntity(dto);
        Account createdAccount = accountService.saveAccount(newAccount);
        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(createdAccount)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<Account>> updateAccountBalance
            (@PathVariable String id, @Valid @RequestBody UpdateBalanceDTO dto) {
        //TODO: validate id is not blank else throw resource not found
        //TODO: Map dto to account
        //TODO: add id to account
        //TODO: invoke service to update balance
        //TODO: Create ApiResponseDTO and add modified account to data
        //TODO: Return modified account data with status 200
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteAccount(String id) {
        return null;
    }
}
