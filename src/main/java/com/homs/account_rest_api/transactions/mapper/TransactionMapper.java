package com.homs.account_rest_api.transactions.mapper;


import com.homs.account_rest_api.transactions.dto.CreateTransaction;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.transactions.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class TransactionMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Transaction toEntity(CreateTransaction dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        return Transaction.builder()
                .amount(dto.getAmount())
                .type(TransactionType.valueOf(dto.getType().toUpperCase()))
                .date(LocalDate.parse(dto.getDate(), DATE_FORMATTER))
                .alias(dto.getAlias())
                .build();
    }

    public TransactionDto toTransactionDto(Transaction transaction) {
        if (Objects.isNull(transaction)) {
            return null;
        }

        return TransactionDto.builder()
                .id(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .description(transaction.getAlias())
                .build();
    }
}
