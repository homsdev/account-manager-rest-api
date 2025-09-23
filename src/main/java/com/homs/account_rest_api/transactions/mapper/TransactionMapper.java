package com.homs.account_rest_api.transactions.mapper;


import com.homs.account_rest_api.accounts.dto.AccountDTO;
import com.homs.account_rest_api.accounts.mapper.AccountMapper;
import com.homs.account_rest_api.transactions.dto.CreateTransactionRequest;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionMapper {
    public static Transaction toEntity(CreateTransactionRequest dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        return Transaction.builder()
                .transactionId(null)
                .amount(dto.getAmount())
                .type(TransactionType.valueOf(dto.getType()))
                .date(LocalDate.parse(dto.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .alias(dto.getAlias())
                .category(null)
                .account(null)
                .build();
    }

    public static TransactionDto toTransactionDto(Transaction transaction) {
        if (Objects.isNull(transaction)) {
            return null;
        }

        AccountDTO accountDTO = AccountMapper.toDTO(transaction.getAccount());


        return TransactionDto.builder()
                .id(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .alias(transaction.getAlias())
                .accountDTO(accountDTO)
                .build();
    }
}
