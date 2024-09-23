package com.homs.account_rest_api.mapper;

import com.homs.account_rest_api.dto.CreateAccountDto;
import com.homs.account_rest_api.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    @Mapping(source = "balance",target = "accountBalance")
    @Mapping(source = "alias",target = "accountAlias")
    CreateAccountDto toDto(Account account);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(source = "accountAlias",target = "alias")
    @Mapping(source = "accountBalance",target = "balance")
    Account toEntity(CreateAccountDto accountDto);
}
