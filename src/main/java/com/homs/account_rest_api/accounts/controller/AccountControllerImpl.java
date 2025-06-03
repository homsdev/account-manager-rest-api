package com.homs.account_rest_api.accounts.controller;


import com.homs.account_rest_api.accounts.dto.UpdateBalanceDTO;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.service.AccountService;

import com.homs.account_rest_api.dto.ApiResponse;
import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    private void expandSelfResponse(ApiResponseDTO<Account> responseDTO, String id) {

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).getAccountById(id)
        ).withSelfRel());

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).updateAccountBalance(id, null)
        ).withRel("update").withType("PATCH"));

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).deleteAccount(id)
        ).withRel("delete").withType("DELETE"));

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).getAllAccounts()
        ).withRel("all_accounts"));

    }

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

        response.add(linkTo(
                methodOn(AccountControllerImpl.class).createNewAccount(null)
        ).withRel("create").withType("POST"));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<Account>> getAccountById(@PathVariable("id") String id) {
        log.info("Executing find by id for: {}", id);
        if (id.isBlank()) {
            throw new ResourceNotFoundException(id);
        }
        Account account = accountService.findById(id);
        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(account)
                .build();

        expandSelfResponse(response, account.getAccountId());

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

        expandSelfResponse(response, createdAccount.getAccountId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<Account>> updateAccountBalance
            (@PathVariable String id, @Valid @RequestBody UpdateBalanceDTO dto) {
        if (id.isBlank()) {
            throw new ResourceNotFoundException(id);
        }

        Account accountToUpdate = accountMapper.toEntity(dto);
        accountToUpdate.setAccountId(id);

        Account account = accountService.updateBalance(accountToUpdate);

        ApiResponseDTO<Account> response = ApiResponseDTO.<Account>builder()
                .data(account)
                .build();

        expandSelfResponse(response, account.getAccountId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        if (id.isBlank()) {
            throw new ResourceNotFoundException(id);
        }

        accountService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
