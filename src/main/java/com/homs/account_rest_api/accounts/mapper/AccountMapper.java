package com.homs.account_rest_api.accounts.mapper;

import com.homs.account_rest_api.accounts.dto.CreateAccountDto;
import com.homs.account_rest_api.accounts.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@SuppressWarnings("unused")
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
