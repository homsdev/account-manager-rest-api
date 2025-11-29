package com.homs.account_rest_api.accounts.controller;


import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.service.AccountService;

import com.homs.account_rest_api.dto.ApiResponseDTO;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
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
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    private void expandSelfResponse(ApiResponseDTO<AccountDTO> responseDTO, String id) {

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).getAccountById(id)
        ).withSelfRel());

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).deleteAccount(id)
        ).withRel("delete").withType("DELETE"));

        responseDTO.add(linkTo(
                methodOn(AccountControllerImpl.class).getAllAccounts()
        ).withRel("all_accounts"));

    }

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDTO<List<AccountDTO>>> getAllAccounts() {
        log.info("Executing getAllAccounts Controller");
        List<AccountDTO> all = accountService.findAll();

        if (all.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponseDTO<List<AccountDTO>> response =
                ApiResponseDTO.<List<AccountDTO>>builder()
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
    public ResponseEntity<ApiResponseDTO<AccountDTO>> getAccountById(@PathVariable("id") String id) {
        log.info("Executing find by id for: {}", id);
        if (id.isBlank()) {
            throw new ResourceNotFoundException(id);
        }
        AccountDTO account = accountService.findById(id);
        ApiResponseDTO<AccountDTO> response = ApiResponseDTO.<AccountDTO>builder()
                .data(account)
                .build();

        expandSelfResponse(response, account.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseDTO<AccountDTO>> createNewAccount(@RequestBody AccountDTO dto) {
        log.info("Executing createNewAccount");
        AccountDTO createdAccount = accountService.saveAccount(dto);
        ApiResponseDTO<AccountDTO> response = ApiResponseDTO.<AccountDTO>builder()
                .data(createdAccount)
                .build();

        expandSelfResponse(response, createdAccount.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDTO<AccountDTO>> updateAccountBalance
            (@PathVariable String id, @RequestBody AccountDTO dto) {

        AccountDTO updatedAccountDTO = accountService.updateBalance(id, dto);

        ApiResponseDTO<AccountDTO> response = ApiResponseDTO.<AccountDTO>builder()
                .data(updatedAccountDTO)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
