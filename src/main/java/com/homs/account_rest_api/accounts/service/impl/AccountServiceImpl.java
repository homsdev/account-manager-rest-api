package com.homs.account_rest_api.accounts.service.impl;

import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.dto.CreateAccount;
import com.homs.account_rest_api.accounts.dto.UpdateBalance;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.accounts.model.Account;
import com.homs.account_rest_api.accounts.repository.AccountRepository;
import com.homs.account_rest_api.accounts.service.AccountService;
import com.homs.account_rest_api.exception.InvalidParametersException;
import com.homs.account_rest_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    @Override
    public List<AccountDTO> findAll() {

        List<Account> accountList = accountRepository.findAll();

        if (accountList.isEmpty()) return Collections.emptyList();

        return accountList.stream()
                .map(accountMapper::toDTO).toList();
    }

    @Override
    public AccountDTO findById(Long id) {

        if (id == null) throw new InvalidParametersException("Id cannot be null");

        Account account = accountRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        return accountMapper.toDTO(account);
    }

    @Override
    public AccountDTO saveAccount(CreateAccount dto) {
        List<String> errorList = validateCreateAccount(dto);

        if (!errorList.isEmpty()) {
            throw new InvalidParametersException(errorList);
        }

        Account newAccount = accountRepository.save(accountMapper.toEntity(dto))
                .orElseThrow(() -> new DataIntegrityViolationException("Failed to save account"));

        return accountMapper.toDTO(newAccount);
    }

    @Override
    public Integer deleteById(Long id) {
        Integer deletedCount = accountRepository.deleteById(id);

        if (deletedCount == 0) throw new ResourceNotFoundException();

        return accountRepository.deleteById(id);
    }

    @Override
    public AccountDTO updateBalance(Long id, UpdateBalance dto) {
        List<String> errorMessages = validateUpdateBalance(id, dto);

        if (!errorMessages.isEmpty()) {
            throw new InvalidParametersException(errorMessages);
        }

        Account accountToUpdate = Account.builder().accountId(id).balance(dto.getBalance()).build();

        Account updatedAccount = accountRepository.updateBalance(accountToUpdate)
                .orElseThrow(() -> new DataIntegrityViolationException("Failed to update balance for account: "));

        return accountMapper.toDTO(updatedAccount);
    }


    private List<String> validateCreateAccount(CreateAccount dto) {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(dto.getAlias()) || dto.getAlias().isBlank()) {
            errors.add("Account alias cannot be null or blank");
        }

        if (Objects.isNull(dto.getType())) {
            errors.add("Account type cannot be null");
        }

        if (Objects.isNull(dto.getBalance())) {
            errors.add("Account balance cannot be null");
        }

        return errors;
    }

    private List<String> validateUpdateBalance(Long id, UpdateBalance dto) {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(id)) {
            errors.add("Missing account id");
        }

        if (Objects.isNull(dto.getBalance())) {
            errors.add("Balance cannot be null");
        }

        return errors;
    }
}
