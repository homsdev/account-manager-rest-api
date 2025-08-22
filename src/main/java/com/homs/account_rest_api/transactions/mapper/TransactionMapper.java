package com.homs.account_rest_api.transactions.mapper;


import com.homs.account_rest_api.transactions.dto.CreateTransactionDTO;
import com.homs.account_rest_api.transactions.dto.TransactionDto;
import com.homs.account_rest_api.transactions.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(source = "description", target = "alias")
    @Mapping(source = "categoryDTO", target = "category")
    Transaction toEntity(CreateTransactionDTO dto);

    @Mapping(source = "transactionId",target = "id")
    @Mapping(source = "amount",target = "amount")
    @Mapping(source = "type",target = "type")
    @Mapping(source = "date",target = "date")
    @Mapping(source = "alias",target = "alias")
    TransactionDto toTransactionDto(Transaction transaction);
}
