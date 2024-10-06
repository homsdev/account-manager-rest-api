package com.homs.account_rest_api.controller.impl;

import com.homs.account_rest_api.controller.AccountController;
import com.homs.account_rest_api.dto.CreateAccountDto;
import com.homs.account_rest_api.mapper.AccountMapper;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.ApiResponseDTO;
import com.homs.account_rest_api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Account>> getAllAccounts() {
        List<Account> result = accountService.findAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                    .data(result)
                    .message("Retrieving all accounts")
                    .code(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Account>> getAccountById(@PathVariable String id) {
        Account account = accountService.findById(id);

        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(Collections.singletonList(account))
                .message("Account found")
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Account>> createNewAccount(
            @RequestBody @Valid CreateAccountDto dto
    ) {
        Account newAccount = accountService.saveAccount(accountMapper.toEntity(dto));
        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(Collections.singletonList(newAccount))
                .message("Account creation successful")
                .code(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Account>> updateAccountBalance(@PathVariable String id, @RequestBody CreateAccountDto dto) {
        Account accountToUpdate = accountMapper.toEntity(dto);
        accountToUpdate.setAccountId(id);
        Account result = accountService.updateBalance(accountToUpdate);
        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(Collections.singletonList(result))
                .message("Account balance updated correctly")
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
