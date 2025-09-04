package com.homs.account_rest_api.accounts.service;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotCreatedException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Account service class to handle basic CRUD operations
 * for domain {@link Account}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private List<String> validateAccountDTO(AccountDTO dto) {
        List<String> messages = new ArrayList<>();

        if (dto == null) {
            messages.add("Account data is required");
            return messages;
        }

        if (dto.getAlias() == null) {
            messages.add("Account alias is required");
        } else if (dto.getAlias().isBlank()) {
            messages.add("Account alias cannot be blank");
        } else if (dto.getAlias().length() > 50) {
            messages.add("Account alias cannot exceed 50 characters");
        } else if (!dto.getAlias().matches("^[a-zA-Z0-9\\s\\-._]+$")) {
            messages.add("Account alias can only contain letters, numbers, spaces, hyphens, dots and underscores");
        }

        if (dto.getBalance() == null) {
            messages.add("Account balance is required");
        } else {
            if (dto.getBalance().scale() > 2) {
                messages.add("Account balance cannot have more than 2 decimal places");
            }
        }

        return messages;
    }

    /**
     * Retrieves all accounts in database or an empty {@link List}
     *
     * @return A {@link List} of {@link AccountDTO} containing all found accounts
     */
    public List<AccountDTO> findAll() {
        log.info("Executing findAll service");
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            log.info("No account data was found");
            return Collections.emptyList();
        }

        return accounts.stream().map(AccountMapper::toDTO).toList();
    }

    /**
     * Finds an account into database by the provided id
     *
     * @param id Unique identifier of the account
     * @return The {@link AccountDTO} associated with the given id
     * @throws ResourceNotFoundException if no account is found
     */
    public AccountDTO findById(String id) {
        log.info("Executing findById service with id: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return AccountMapper.toDTO(account);
    }

    /**
     * Saves new account data into database
     *
     * @param newAccount {@link AccountDTO} object containing the info of the new account
     * @return The {@link AccountDTO} persisted in DB
     * @throws RuntimeException if account was not created
     */
    public AccountDTO saveAccount(AccountDTO newAccount) {
        log.info("Executing saveAccount service");

        List<String> errors = validateAccountDTO(newAccount);
        if (!errors.isEmpty()) {
            throw new InvalidParametersException(errors);
        }

        Account entity = AccountMapper.toEntity(newAccount);

        Account savedAccount = accountRepository.save(entity)
                .orElseThrow(() -> new ResourceNotCreatedException("Failed to create new account"));

        return AccountMapper.toDTO(savedAccount);
    }

    /**
     * Deletes account from database by the provided id
     *
     * @param id Unique identifier of the account to delete
     * @return {@link Integer} indicating affected rows
     */
    public Integer deleteById(String id) {
        log.info("Executing deleteByIdService with id: {}", id);
        if (id == null || id.isBlank()) {
            throw new InvalidParametersException("Missing account id");
        }
        return accountRepository.deleteById(id);
    }

    /**
     * Updates balance information for the given account
     *
     * @param dto {@link AccountDTO} containing the updated balance information
     * @return {@link AccountDTO} updated account information dto
     * @throws ResourceNotFoundException if update operation is not concluded
     */
    @Transactional
    public AccountDTO updateBalance(String id, AccountDTO dto) {
        log.info("Executing updateBalance service");
        List<String> errorMessages = new ArrayList<>();

        if (id == null) {
            errorMessages.add("Missing account id");
        } else if (id.isBlank()) {
            errorMessages.add("Account id cannot be empty");
        }

        if (dto == null) {
            errorMessages.add("Missing account information");
        } else {
            if (dto.getBalance() == null) {
                errorMessages.add("Missing account balance");
            }
        }

        if (!errorMessages.isEmpty()) {
            log.warn("Validation failed for updateBalance service");
            throw new InvalidParametersException(errorMessages);
        }

        Account entity = AccountMapper.toEntity(dto);
        entity.setAccountId(id);

        Account accountToUpdate = accountRepository.updateBalance(entity)
                .orElseThrow(() -> {
                    log.warn("Error when updating balance: Account not found");
                    return new ResourceNotFoundException("Requested account not found");
                });
        return AccountMapper.toDTO(accountToUpdate);
    }

}
