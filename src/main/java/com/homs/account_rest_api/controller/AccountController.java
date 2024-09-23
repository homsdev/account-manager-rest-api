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
        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .data(result)
                .message(HttpStatus.OK.getReasonPhrase())
                .code(HttpStatus.OK.value())
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        Account account = accountService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(account);
    }

    @PostMapping
    public ResponseEntity<Account> createNewAccount(
            @RequestBody CreateAccountDto dto
    ) {
        Account newAccount = accountService.saveAccount(accountMapper.toEntity(dto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccountBalance(@PathVariable String id, @RequestBody CreateAccountDto dto) {
        Account accountToUpdate = accountMapper.toEntity(dto);
        accountToUpdate.setAccountId(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.updateBalance(accountToUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
