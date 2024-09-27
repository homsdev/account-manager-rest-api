package com.homs.account_rest_api.controller;

import com.homs.account_rest_api.dto.CreateAccountDto;
import com.homs.account_rest_api.mapper.AccountMapper;
import com.homs.account_rest_api.model.Account;
import com.homs.account_rest_api.model.ApiResponse;
import com.homs.account_rest_api.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountMapper accountMapper;
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<Account>> getAllAccounts() {
        List<Account> result = accountService.findAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            ApiResponse<Account> response = ApiResponse.<Account>builder()
                    .data(result)
                    .message("Retrieving all accounts")
                    .code(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Account>> getAccountById(@PathVariable String id) {
        Account account = accountService.findById(id);

        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .data(Collections.singletonList(account))
                .message("Account found")
                .code(HttpStatus.OK.value())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Account>> createNewAccount(
            @RequestBody CreateAccountDto dto
    ) {
        Account newAccount = accountService.saveAccount(accountMapper.toEntity(dto));
        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .data(Collections.singletonList(newAccount))
                .message("Account creation successful")
                .code(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Account>> updateAccountBalance(@PathVariable String id, @RequestBody CreateAccountDto dto) {
        Account accountToUpdate = accountMapper.toEntity(dto);
        accountToUpdate.setAccountId(id);
        Account result = accountService.updateBalance(accountToUpdate);
        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .data(Collections.singletonList(result))
                .message("Account balance updated correctly")
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
