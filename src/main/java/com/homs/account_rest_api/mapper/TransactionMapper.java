package com.homs.account_rest_api.mapper;

import com.homs.account_rest_api.dto.CreateTransactionDTO;
import com.homs.account_rest_api.enums.TransactionType;
import com.homs.account_rest_api.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mapping(source = "description", target = "alias")
    @Mapping(source = "date", target = "date", qualifiedByName = "stringToDate")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "transactionType", target = "type", qualifiedByName = "stringToTransactionType")
    Transaction toEntity(CreateTransactionDTO dto);

    @Named("stringToDate")
    default LocalDate stringToDate(String date) {
        return LocalDate.parse(date);
    }

    @Named("stringToTransactionType")
    default TransactionType stringToTransactionType(String transactionType) {
        return TransactionType.valueOf(transactionType.toUpperCase());
    }
}
