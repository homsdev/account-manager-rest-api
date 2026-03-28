package com.homs.account_rest_api.accounts.controller;


import com.homs.account_rest_api.accounts.dto.*;
import com.homs.account_rest_api.accounts.service.AccountService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountControllerV1Impl implements AccountController {

    private final AccountService accountService;

    @GetMapping
    @Override
    public ResponseEntity<AccountListResponseDTO> getAllAccounts() {
        log.info("Executing getAllAccounts Controller");
        List<AccountDTO> all = accountService.findAll();

        if (all.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        AccountListResponseDTO response = AccountListResponseDTO.builder()
                .data(all)
                .timestamp(Instant.now())
                .build();

        response.add(linkTo(
                methodOn(AccountControllerV1Impl.class).createNewAccount(null)
        ).withRel("create").withType("POST"));

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable("id") Long id) {
        log.info("Executing find by id for: {}", id);
        AccountDTO account = accountService.findById(id);
        AccountResponseDTO res = AccountResponseDTO.builder().data(account)
                .timestamp(Instant.now()).build();
        return ResponseEntity.ok(res);
    }

    @PostMapping
    @Override
    public ResponseEntity<AccountResponseDTO> createNewAccount(@RequestBody CreateAccount dto) {
        log.info("Executing createNewAccount");

        AccountDTO createdAccount = accountService.saveAccount(dto);

        AccountResponseDTO response = AccountResponseDTO.builder()
                .data(createdAccount)
                .timestamp(Instant.now())
                .build();

        response.add(linkTo(
                methodOn(AccountControllerV1Impl.class).getAccountById(createdAccount.getId())
        ).withSelfRel());

        response.add(linkTo(
                methodOn(AccountControllerV1Impl.class).deleteAccount(createdAccount.getId())
        ).withRel("delete").withType("DELETE"));

        response.add(linkTo(
                methodOn(AccountControllerV1Impl.class).getAllAccounts()
        ).withRel("all_accounts"));


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<AccountResponseDTO> updateAccountBalance
            (@PathVariable Long id, @RequestBody UpdateBalance dto) {

        log.info("Executing updateAccountBalance for id: {}", id);

        AccountDTO updatedAccountDTO = accountService.updateBalance(id, dto);

        AccountResponseDTO response = AccountResponseDTO.builder()
                .data(updatedAccountDTO).timestamp(Instant.now()).build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        log.info("Executing deleteAccount for id: {}", id);

        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
